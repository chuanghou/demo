package com.stellariver.milky.demo.domain;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.enums.CancelBid;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.NewBid;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.domain.support.command.CommandBus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class RealtimeBidProcessor implements EventHandler<RtBidContainer> {


    private final Disruptor<RtBidContainer> disruptor = new Disruptor<>(RtBidContainer::new, 1024, DaemonThreadFactory.INSTANCE);


    public RealtimeBidProcessor() {
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


    public final PriorityQueue<NewBid> buyPriorityQueue = new PriorityQueue<>(buyComparator);

    public final PriorityQueue<NewBid> sellPriorityQueue = new PriorityQueue<>(sellComparator);


    public void post(NewBid newBid) {
        disruptor.publishEvent((rtBidContainer, sequence) -> {
            rtBidContainer.setCancelBid(null);
            rtBidContainer.setNewBid(newBid);
        });
    }

    public void post(CancelBid cancelBid) {
        disruptor.publishEvent((rtBidContainer, sequence) -> {
            rtBidContainer.setCancelBid(cancelBid);
            rtBidContainer.setNewBid(null);
        });
    }

    @Override
    public void onEvent(RtBidContainer event, long sequence, boolean endOfBatch) throws Exception {
        if (event.getCancelBid() == null) {
            doProcessNewBid(event.getNewBid());
        } else {
            doProcessCancel(event.getCancelBid());
        }
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
        Deal deal = Deal.builder().bidId(newBid.getBidId())
                .unitId(newBid.getUnitId()).quantity(dealQuantity).price(dealPrice).build();
        UnitCommand.DealReport dealReport = UnitCommand.DealReport.builder()
                .unitId(newBid.getUnitId()).deals(Collect.asList(deal)).build();
        CompletableFuture.runAsync(() -> CommandBus.accept(dealReport, new HashMap<>()));
    }




}
