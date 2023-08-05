package com.stellariver.milky.demo.domain;

import com.google.common.collect.ListMultimap;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.Clock;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.common.enums.NewBid;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.domain.command.UnitEvent;
import com.stellariver.milky.demo.domain.event.CompEvent;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.domain.support.command.Command;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.domain.support.context.Context;
import com.stellariver.milky.domain.support.event.Event;
import com.stellariver.milky.domain.support.event.EventRouter;
import com.stellariver.milky.domain.support.event.EventRouters;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.*;
import org.mapstruct.Builder;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@CustomLog
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Routers implements EventRouters {

    final Tunnel tunnel;
    final UniqueIdBuilder uniqueIdBuilder;
    final DelayExecutor delayExecutor;


    @EventRouter
    public void route(CompEvent.Created event, Context context) {
        buildUnits(event, event.getComp());
    }

    public void buildUnits(Event event, Comp comp) {

        IntStream.range(0, comp.getUserTotal()).forEach(userId -> {
            Set<Integer> metaUnitSourceIds = allocateSourceId(comp.getRoundId(), comp.getUserTotal(), userId);
            Set<Integer> metaUnitIds = tunnel.getMetaUnitIdBySourceIds(metaUnitSourceIds);
            BizEx.trueThrow(metaUnitIds.size() != 4, ErrorEnums.SYS_EX.message("应该分配是个4个机组负荷"));
            Map<Integer, AbstractMetaUnit> metaUnits = tunnel.getMetaUnitsByIds(new HashSet<>(metaUnitIds));
            long size = metaUnits.values().stream().map(metaUnit -> Pair.of(metaUnit.getProvince(), metaUnit.getUnitType())).distinct().count();
            BizEx.trueThrow(Kit.notEq(size, 4L), ErrorEnums.CONFIG_ERROR.message("单元分配问题"));
            metaUnitIds.forEach(metaUnitId -> {
                UnitCommand.Create command = UnitCommand.Create.builder()
                        .unitId(uniqueIdBuilder.get())
                        .userId(userId)
                        .compId(comp.getCompId())
                        .roundId(comp.getRoundId())
                        .metaUnit(metaUnits.get(metaUnitId))
                        .build();
                CommandBus.driveByEvent(command, event);
            });
        });
    }


    //TODO 如果机组及负荷数量变化，这里的值需要改
    private Set<Integer> allocateSourceId(Integer roundId, Integer userIdTotal, Integer userId) {
        List<Integer> integers = Allocate.allocateSourceId(roundId, userIdTotal, 30, userId);
        return new HashSet<>(integers);
    }

    @EventRouter
    public void routePush(CompEvent.Started started, Context context) {
        Comp comp = tunnel.runningComp();
        Message message = Message.builder().topic(Topic.STAGE_CHANGE).entity(comp).build();
        tunnel.cast(message);
    }


    @EventRouter
    public void routePush(CompEvent.Stepped stepped, Context context) {
        Comp comp = tunnel.runningComp();
        Message message = Message.builder().topic(Topic.STAGE_CHANGE).entity(comp).build();
        tunnel.cast(message);
    }

    @EventRouter
    public void routeAutoNext(CompEvent.Started started, Context context) {

        Comp comp = context.getByAggregateId(Comp.class, started.getAggregateId());
        Stage currentStage = Stage.builder()
                .roundId(comp.getRoundId())
                .marketType(comp.getMarketType())
                .marketStatus(comp.getMarketStatus())
                .build();
        Duration accumulateDuration = Duration.ZERO;
        long time = Clock.currentTimeMillis();

        Map<Stage, Date> endTime = new HashMap<>();
        do {
            Duration duration = comp.getDurations().get(currentStage.getMarketType()).get(currentStage.getMarketStatus());
            time += duration.get(ChronoUnit.SECONDS) * 1000;
            accumulateDuration = accumulateDuration.plus(duration);
            endTime.put(currentStage, new Date(time));
            Stage nexStage = currentStage.next();
            CompCommand.Step command = CompCommand.Step.builder().nextStage(nexStage).compId(comp.getCompId()).build();
            DelayCommandWrapper delayCommandWrapper = new DelayCommandWrapper(command, new Date(time));
            System.out.println("DELAY " + delayCommandWrapper.getExecuteDate().toString());
            delayExecutor.delayQueue.add(delayCommandWrapper);
            currentStage = nexStage;
        } while (!currentStage.lastOne(comp.getRoundTotal()));

        delayExecutor.start();

        CompCommand.TimeLine command = CompCommand.TimeLine.builder().compId(comp.getCompId()).endTime(endTime).build();
        CommandBus.driveByEvent(command, started);
    }

    @EventRouter
    public void routeForMarketDB(CompEvent.Started started, Context context) {
        Comp comp = tunnel.runningComp();
        tunnel.updateRoundIdForMarketSetting(comp.getRoundId());
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class DelayCommandWrapper implements Delayed {

        private Command command; // 消息内容
        private Date executeDate;// 延迟时长，这个是必须的属性因为要按照这个判断延时时长。

        public DelayCommandWrapper(Command command, Date executeDate) {
            this.command = command;
            this.executeDate = executeDate;
        }

        @Override
        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(executeDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(@NonNull Delayed o) {
            return Long.compare(executeDate.getTime(), ((DelayCommandWrapper) o).getExecuteDate().getTime());
        }

    }

    @Component
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class DelayExecutor implements Runnable{

        final DelayQueue<DelayCommandWrapper> delayQueue = new DelayQueue<>();
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final AtomicBoolean started = new AtomicBoolean(false);
        public void start() {
            boolean b = started.compareAndSet(false, true);
            if (b) {
                executorService.execute(this);
            }
        }

        @Override
        public void run() {

            while (true) {
                DelayCommandWrapper delayCommandWrapper;
                try {
                    delayCommandWrapper = delayQueue.take();
                } catch (InterruptedException e) {
                    throw new SysEx(e);
                }
                Command command = delayCommandWrapper.getCommand();
                CommandBus.accept(command, new HashMap<>());
            }

        }

    }


    @EventRouter
    public void routeForClear(CompEvent.Stepped stepped, Context context) {
        Comp comp = tunnel.runningComp();
        boolean b0 = stepped.getLastMarketType() == MarketType.INTER_ANNUAL_PROVINCIAL;
        boolean b1 = stepped.getLastMarketType() == MarketType.INTER_MONTHLY_PROVINCIAL;
        boolean b2 = stepped.getLastMarketStatus() == Status.MarketStatus.OPEN;
        if ((b0 || b1) && b2) {
            List<Unit> units = tunnel.listUnitsByCompId(stepped.getCompId());
            units.forEach(unit -> {
                UnitCommand.CentralizedTrigger command = UnitCommand.CentralizedTrigger.builder()
                        .unitId(unit.getUnitId()).marketType(stepped.getLastMarketType()).build();
                CommandBus.driveByEvent(command, stepped);
            });
            CompCommand.Clear clear = CompCommand.Clear.builder().compId(comp.getCompId()).build();
            CommandBus.driveByEvent(clear, stepped);
        }
    }

    @EventRouter
    public void routeForNexRound(CompEvent.Stepped stepped, Context context) {
        Comp comp = tunnel.runningComp();
        if (stepped.getLastMarketType() == MarketType.FINAL_CLEAR) {
            buildUnits(stepped, comp);
        }
    }

    @EventRouter
    public void routeForMarketDB(CompEvent.Stepped stepped, Context context) {
        Comp comp = tunnel.runningComp();
        if (stepped.getLastMarketType() == MarketType.FINAL_CLEAR) {
            tunnel.updateRoundIdForMarketSetting(comp.getRoundId());
        }
    }


    @EventRouter
    public void routeRealtimeMarketClose(CompEvent.Stepped stepped, Context context) {
        boolean b0 = stepped.getLastMarketType() == MarketType.INTRA_ANNUAL_PROVINCIAL;
        boolean b1 = stepped.getLastMarketType() == MarketType.INTRA_MONTHLY_PROVINCIAL;
        boolean b2 = stepped.getLastMarketStatus() == Status.MarketStatus.OPEN;
        if ((b0 || b1) && b2) {
            Comp comp = tunnel.runningComp();
            CompCommand.RtMarketClose command = CompCommand.RtMarketClose.builder().compId(comp.getCompId()).build();
            CommandBus.driveByEvent(command, stepped);
        }
    }


    @EventRouter
    public void route(UnitEvent.CentralizedTriggered event, Context context) {
        CompCommand.CentralizedBid command = CompCommand.CentralizedBid.builder()
                .unitId(event.getUnitId()).compId(event.getCompId()).marketType(event.getMarketType()).bids(event.getBids()).build();
        CommandBus.driveByEvent(command, event);
    }


    @EventRouter
    public void routeForDealReport(CompEvent.Cleared event, Context context) {
        ListMultimap<Long, Deal> dealMultiMap = event.getCentralizedDealsMap().values().stream()
                .map(CentralizedDeals::getDeals).flatMap(Collection::stream).collect(Collect.listMultiMap(Deal::getUnitId));
        dealMultiMap.keySet().forEach(unitId -> {
            List<Deal> deals = dealMultiMap.get(unitId);
            UnitCommand.DealReport command = UnitCommand.DealReport.builder().unitId(unitId).deals(deals).build();
            CommandBus.driveByEvent(command, event);
        });
    }

    @EventRouter
    public void routeForReplenishes(CompEvent.Cleared event, Context context) {
        Comp comp = tunnel.runningComp();
        Map<TimeFrame, Double> replenishes = event.getReplenishes();
        tunnel.writeReplenishes(comp.getRoundId(), comp.getMarketType(), replenishes);
    }

    @EventRouter
    public void route(UnitEvent.RtCancelBidDeclared event, Context context) {

        CompCommand.RtCancelBid command = CompCommand.RtCancelBid.builder()
                .compId(event.getCompId())
                .bidId(event.getBid().getBidId())
                .province(event.getBid().getProvince())
                .unitId(event.getUnitId())
                .bidDirection(event.getBid().getDirection())
                .timeFrame(event.getBid().getTimeFrame())
                .build();
        CommandBus.driveByEvent(command, event);
    }


    @EventRouter
    public void route(UnitEvent.RtBidDeclared event, Context context) {
        Comp comp = tunnel.runningComp();
        CompCommand.RtNewBid command = CompCommand.RtNewBid.builder()
                .compId(comp.getCompId())
                .newBid(Convertor.INST.to(event.getBid()))
                .build();
        CommandBus.driveByEvent(command, event);
    }

    @EventRouter
    public void route(UnitEvent.DealReported event, Context context) {
        boolean b0 = tunnel.runningComp().getMarketType() == MarketType.INTRA_ANNUAL_PROVINCIAL;
        boolean b1 = tunnel.runningComp().getMarketType() == MarketType.INTRA_MONTHLY_PROVINCIAL;
        if (!(b0 || b1)) {
            return;
        }
        Message message = Message.builder()
                .topic(Topic.RT_UNIT).userId(event.getUnit().getUserId().toString()).entity(event.getUnit()).build();
        tunnel.push(message);
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        NewBid to(Bid bid);

        // 主机收到的报单时间，而不是客户机申报的时间
        @AfterMapping
        default void after(Bid bid, NewBid newBid) {
            newBid.setDate(Clock.now());
        }


    }
}
