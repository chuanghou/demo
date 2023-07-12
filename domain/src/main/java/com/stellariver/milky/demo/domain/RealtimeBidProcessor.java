package com.stellariver.milky.demo.domain;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.stellariver.milky.common.tool.common.Typed;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.common.Stage;
import com.stellariver.milky.demo.basic.TypedEnums;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.TxGroup;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.domain.support.command.CommandBus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;

public class RealtimeBidProcessor implements EventHandler<RealtimeBidContainer> {


    private final Disruptor<RealtimeBidContainer> disruptor = new Disruptor<>(RealtimeBidContainer::new, 1024, DaemonThreadFactory.INSTANCE);


    public RealtimeBidProcessor() {
        disruptor.handleEventsWith(this);
        disruptor.start();
    }

    static private final Comparator<Bid> buyComparator = (o1, o2) -> {
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

    static private final Comparator<Bid> sellComparator = (o1, o2) -> {
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


    public final PriorityQueue<Bid> buyPriorityQueue = new PriorityQueue<>(buyComparator);

    public final PriorityQueue<Bid> sellPriorityQueue = new PriorityQueue<>(sellComparator);


    public void post(Bid bid) {
        disruptor.publishEvent((realtimeBidContainer, sequence) -> realtimeBidContainer.setBid(bid));
    }

    @Override
    public void onEvent(RealtimeBidContainer event, long sequence, boolean endOfBatch) throws Exception {
        Bid bid = event.getBid();
        if (bid.getDirection() == Direction.BUY) {
            buyPriorityQueue.add(bid);
        } else if (bid.getDirection() == Direction.SELL) {
            sellPriorityQueue.add(bid);
        } else {
            throw new RuntimeException();
        }
        Bid buyBid = buyPriorityQueue.peek();
        Bid sellBid = sellPriorityQueue.peek();

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

        System.out.println(buyBid.getId() +" buy deal " + dealQuantity);
        System.out.println(sellBid.getId() + " sell deal " + dealQuantity);
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

        TxGroup txGroup = buyBid.getTxGroup();
        UnitCommand.DealReport buyBidDealReport = UnitCommand.DealReport.builder()
                .bidId(buyBid.getId())
                .txGroup(txGroup)
                .deal(Deal.builder().quantity(dealQuantity).price(dealPrice).build())
                .build();
        Map<Class<? extends Typed<?>>, Object> parameters0 = Collect.asMap(TypedEnums.STAGE.class, buyBid.getStage());
        CompletableFuture.runAsync(() -> CommandBus.accept(buyBidDealReport, parameters0));
        UnitCommand.DealReport sellBidDealReport = UnitCommand.DealReport.builder()
                .bidId(sellBid.getId())
                .txGroup(txGroup)
                .deal(Deal.builder().quantity(dealQuantity).price(dealPrice).build())
                .build();
        Map<Class<? extends Typed<?>>, Object> parameters1 = Collect.asMap(TypedEnums.STAGE.class, buyBid.getStage());
        CompletableFuture.runAsync(() -> CommandBus.accept(sellBidDealReport, parameters1));
    }
}
