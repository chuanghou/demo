package com.stellariver.milky.demo.domain;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.stellariver.milky.common.base.BeanUtil;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.Clock;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.enums.*;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RealtimeBidProcessor implements EventHandler<RtBidContainer> {

    final RtProcessorKey rtProcessorKey;

    final Disruptor<RtBidContainer> disruptor = new Disruptor<>(RtBidContainer::new, 1024, DaemonThreadFactory.INSTANCE);

    final PriorityQueue<NewBid> buyPriorityQueue = new PriorityQueue<>(buyComparator);

    final PriorityQueue<NewBid> sellPriorityQueue = new PriorityQueue<>(sellComparator);

    Double currentPrice;
    final List<Deal> deals = new ArrayList<>();

    final RtCompVO rtCompVO = new RtCompVO();

    public RealtimeBidProcessor(RtProcessorKey rtProcessorKey) {
        this.rtProcessorKey = rtProcessorKey;
        disruptor.handleEventsWith(this);
        disruptor.start();
    }

    static private final Comparator<NewBid> buyComparator = (o1, o2) -> {
        if (o1.getPrice() > o2.getPrice()) {
            return -1;
        } else if (o1.getPrice() < o2.getPrice()) {
            return 1;
        } else {
            if (o1.getDate().after(o2.getDate())) {
                return 1;
            } else if (o2.getDate().after(o1.getDate())){
                return -1;
            } else {
                return 0;
            }
        }
    };

    static private final Comparator<NewBid> sellComparator = (o1, o2) -> {
        if (o1.getPrice() < o2.getPrice()) {
            return -1;
        } else if (o1.getPrice() > o2.getPrice()) {
            return 1;
        } else {
            if (o1.getDate().after(o2.getDate())) {
                return 1;
            } else if (o2.getDate().after(o1.getDate())){
                return -1;
            } else {
                return 0;
            }
        }
    };


    public void post(NewBid newBid) {
        disruptor.publishEvent((rtBidContainer, sequence) -> {
            rtBidContainer.setClose(false);
            rtBidContainer.setCancelBid(null);
            rtBidContainer.setNewBid(newBid);
        });
    }

    public void post(CancelBid cancelBid) {
        disruptor.publishEvent((rtBidContainer, sequence) -> {
            rtBidContainer.setClose(false);
            rtBidContainer.setCancelBid(cancelBid);
            rtBidContainer.setNewBid(null);
        });
    }

    public void close() {
        disruptor.publishEvent((rtBidContainer, sequence) -> {
            rtBidContainer.setClose(true);
            rtBidContainer.setCancelBid(null);
            rtBidContainer.setNewBid(null);
        });
    }


    @Override
    public void onEvent(RtBidContainer event, long sequence, boolean endOfBatch) {
        updateRtCompPushDetail();
        CompletableFuture.runAsync(this::pushRtCompVO);
        if (event.getNewBid() != null){
            doProcessNewBid(event.getNewBid());
        } else if (event.getCancelBid() != null) {
            doProcessCancel(event.getCancelBid());
        } else if (event.getClose()) {
            doClose();
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }
        updateRtCompPushDetail();
    }

    private void doClose() {

        buyPriorityQueue.forEach(bid -> {
            UnitCommand.RtBidCancelled command = UnitCommand.RtBidCancelled.builder()
                    .bidId(bid.getBidId()).unitId(bid.getUnitId()).remainder(bid.getQuantity()).build();
            CommandBus.accept(command, new HashMap<>());
        });

        sellPriorityQueue.forEach(bid -> {
            UnitCommand.RtBidCancelled command = UnitCommand.RtBidCancelled.builder()
                    .bidId(bid.getBidId()).unitId(bid.getUnitId()).remainder(bid.getQuantity()).build();
            CommandBus.accept(command, new HashMap<>());
        });


    }

    private void doProcessCancel(CancelBid cancelBid) {
        PriorityQueue<NewBid> newBids = cancelBid.getDirection() == Direction.BUY ? buyPriorityQueue : sellPriorityQueue;

        AtomicReference<NewBid> cancelledBiz = new AtomicReference<>();
        boolean b = newBids.removeIf(bid -> {
            boolean eq = Kit.eq(cancelBid.getBidId(), bid.getBidId());
            if (eq) {
                cancelledBiz.set(bid);
            }
            return eq;
        });
        SysEx.falseThrow(b, ErrorEnums.SYS_EX.message("could not find bid" + cancelBid));

        UnitCommand.RtBidCancelled command = UnitCommand.RtBidCancelled.builder()
                .bidId(cancelBid.getBidId()).unitId(cancelledBiz.get().getUnitId()).remainder(cancelledBiz.get().getQuantity()).build();
        CommandBus.accept(command, new HashMap<>());
    }

    public void doProcessNewBid(NewBid newBid) {
        if (newBid.getDirection() == Direction.BUY) {
            buyPriorityQueue.add(newBid);
        } else if (newBid.getDirection() == Direction.SELL) {
            sellPriorityQueue.add(newBid);
        } else {
            throw new RuntimeException();
        }
        NewBid buyBid = buyPriorityQueue.peek();
        NewBid sellBid = sellPriorityQueue.peek();

        if (buyBid == null || sellBid == null) {
            return;
        }

        if (buyBid.getPrice() < sellBid.getPrice()) {
            return;
        }
        /*
          the deal price rule is not same with stock market,
          the stock market will use the higher price
         */
        Double dealPrice = buyBid.getDate().after(sellBid.getDate()) ? sellBid.getPrice() : buyBid.getPrice();
        double dealQuantity = Math.min(buyBid.getQuantity(), sellBid.getQuantity());

        double buyBalance = buyBid.getQuantity() - dealQuantity;
        if (buyBalance == 0L) {
            buyPriorityQueue.remove();
        } else {
            buyBid.setQuantity(buyBalance);
        }
        double sellBalance = sellBid.getQuantity() - dealQuantity;
        if (sellBalance == 0L) {
            sellPriorityQueue.remove();
        } else {
            sellBid.setQuantity(sellBalance);
        }
        report(buyBid, dealPrice, dealQuantity);
        report(sellBid, dealPrice, dealQuantity);

    }

    private void report(NewBid newBid, Double dealPrice, double dealQuantity) {
        UniqueIdBuilder uniqueIdBuilder = BeanUtil.getBean(UniqueIdBuilder.class);
        Deal deal = Deal.builder().dealId(uniqueIdBuilder.get()).bidId(newBid.getBidId())
                .unitId(newBid.getUnitId()).quantity(dealQuantity).price(dealPrice).date(Clock.now()).build();
        deals.add(deal);
        currentPrice = dealPrice;
        UnitCommand.DealReport dealReport = UnitCommand.DealReport.builder()
                .unitId(newBid.getUnitId()).deals(Collect.asList(deal)).build();
        CompletableFuture.runAsync(() -> CommandBus.accept(dealReport, new HashMap<>()));
    }

    private void updateRtCompPushDetail() {
        rtCompVO.setCurrentPrice(currentPrice);
        rtCompVO.setDeals(new ArrayList<>(deals));
        rtCompVO.setBuyBids(Collect.transfer(new ArrayList<>(buyPriorityQueue), Convertor.INST::to));
        rtCompVO.setSellBids(Collect.transfer(new ArrayList<>(sellPriorityQueue), Convertor.INST::to));
        List<MarketAsk> buyMarketAsks = buildMarketAsks(rtCompVO.getBuyBids());
        rtCompVO.setBuyMarketAsks(buyMarketAsks);
        List<MarketAsk> sellMarketAsks = buildMarketAsks(rtCompVO.getSellBids());
        rtCompVO.setSellMarketAsks(sellMarketAsks);
    }

    private void pushRtCompVO() {
        Tunnel tunnel = BeanUtil.getBean(Tunnel.class);
        Comp comp = tunnel.runningComp();
        Integer userTotal = comp.getUserTotal();
        for (int i = 0; i < userTotal; i++) {
            Message message = Message.builder().topic(Topic.RT_COMP).userId(String.valueOf(i)).entity(rtCompVO).build();
            tunnel.push(message);
        }
    }


    private List<MarketAsk> buildMarketAsks(List<NewBid> newBids) {
        List<MarketAsk> marketAsks = new ArrayList<>();
        for (int i = 0; i < rtCompVO.getBuyBids().size(); i++) {
            NewBid newBid = rtCompVO.getBuyBids().get(i);
            if (marketAsks.size() == 0) {
                marketAsks.add(MarketAsk.builder().quantity(newBid.getQuantity()).price(newBid.getPrice()).build());
            } else if (marketAsks.size() > 5){
                break;
            } else {
                MarketAsk marketAsk = marketAsks.get(marketAsks.size() - 1);
                if (Kit.eq(marketAsk.getPrice(), newBid.getPrice())) {
                    marketAsk.setQuantity(marketAsk.getQuantity() + newBid.getQuantity());
                } else {
                    marketAsks.add(MarketAsk.builder().quantity(newBid.getQuantity()).price(newBid.getPrice()).build());
                }
            }
        }
        return marketAsks;
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        NewBid to(NewBid bid);

    }

}
