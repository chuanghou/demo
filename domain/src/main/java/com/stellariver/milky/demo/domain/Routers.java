package com.stellariver.milky.demo.domain;

import com.google.common.collect.ListMultimap;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.Allocate;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.domain.command.UnitEvent;
import com.stellariver.milky.demo.domain.event.CompEvent;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.domain.support.context.Context;
import com.stellariver.milky.domain.support.event.Event;
import com.stellariver.milky.domain.support.event.EventRouter;
import com.stellariver.milky.domain.support.event.EventRouters;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Routers implements EventRouters {

    final DomainTunnel domainTunnel;
    final Tunnel tunnel;
    final UniqueIdBuilder uniqueIdBuilder;
    final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


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
    public void route(CompEvent.Started started, Context context) {
        Comp comp = context.getByAggregateId(Comp.class, started.getAggregateId());
        Duration duration = comp.getDurations().get(0).get(MarketType.INTER_ANNUAL_PROVINCIAL);
        Stage next = Stage.builder()
                .roundId(comp.getRoundId())
                .marketType(comp.getMarketType())
                .marketStatus(comp.getMarketStatus())
                .build()
                .next(comp.getRoundTotal());
        scheduledExecutorService.schedule(() -> {

            CompCommand.Step command = CompCommand.Step.builder().compId(started.getCompId()).nextStage(next).build();
            CommandBus.accept(command, new HashMap<>());
        }, duration.getSeconds(), TimeUnit.SECONDS);
    }


    @EventRouter
    public void route(CompEvent.Stepped stepped, Context context) {

        Comp comp = context.getByAggregateId(Comp.class, stepped.getAggregateId());
        Duration duration = comp.getDurations().get(stepped.getNextRoundId()).get(stepped.getNextMarketType());
        scheduledExecutorService.schedule(() -> {
            Stage nexStage = Stage.builder()
                    .roundId(stepped.getNextRoundId())
                    .marketType(stepped.getNextMarketType())
                    .marketStatus(stepped.getNextMarketStatus())
                    .build().next(comp.getRoundTotal());
            if (nexStage.getMarketType() == MarketType.FINAL_CLEAR && nexStage.getMarketStatus() == Status.MarketStatus.OPEN) {
                nexStage = nexStage.next(comp.getRoundTotal());
            }
            CompCommand.Step command = CompCommand.Step.builder()
                    .compId(stepped.getCompId())
                    .nextStage(nexStage)
                    .build();
            CommandBus.accept(command, new HashMap<>());
        }, duration.getSeconds(), TimeUnit.SECONDS);


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

        boolean b3 = stepped.getLastMarketType() == MarketType.FINAL_CLEAR;
        boolean b4 = stepped.getLastMarketStatus() == Status.MarketStatus.CLOSE;
        if (b3 && b4) {
            buildUnits(stepped, comp);
        }

    }

    @EventRouter
    public void route(UnitEvent.CentralizedTriggered event, Context context) {
        CompCommand.CentralizedBid command = CompCommand.CentralizedBid.builder()
                .unitId(event.getUnitId()).compId(event.getCompId()).marketType(event.getMarketType()).bids(event.getBids()).build();
        CommandBus.driveByEvent(command, event);
    }


    @EventRouter
    public void route(CompEvent.Cleared event, Context context) {
        ListMultimap<Long, Deal> dealMultiMap = event.getDeals().stream().collect(Collect.listMultiMap(Deal::getUnitId));
        dealMultiMap.keySet().forEach(unitId -> {
            List<Deal> deals = dealMultiMap.get(unitId);
            UnitCommand.DealReport command = UnitCommand.DealReport.builder().unitId(unitId).deals(deals).build();
            CommandBus.driveByEvent(command, event);
        });

    }

    @EventRouter
    public void route(UnitEvent.RtCancelBidDeclared event, Context context) {

        CompCommand.RtCancelBidDeclare command = CompCommand.RtCancelBidDeclare.builder()
                .compId(event.getCompId())
                .bidId(event.getBid().getBidId())
                .province(event.getBid().getProvince())
                .unitId(event.getUnitId())
                .build();
        CommandBus.driveByEvent(command, event);
    }


    @EventRouter
    public void route(UnitEvent.RtBidDeclared event, Context context) {
        CompCommand.RtNewBidDeclare command = CompCommand.RtNewBidDeclare.builder()
                .unitId(event.getUnitId()).compId(event.getCompId()).bid(event.getBid()).build();
        CommandBus.driveByEvent(command, event);
    }


}
