package com.stellariver.milky.demo;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.domain.Comp;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InterPointTest {

    @Test
    public void testInterPoint() {
        Comp comp = new Comp();
        Bid bid0 = Bid.builder()
                .bidId(0L)
                .price(120D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();


        Bid bid1 = Bid.builder()
                .bidId(1L)
                .price(220D)
                .direction(Direction.SELL)
                .quantity(100D)
                .build();

        Comp.ResolveResult resolveResult = comp.resolveInterPoint(Collect.asList(bid0), Collect.asList(bid1));
        Assertions.assertNull(resolveResult);


        bid0 = Bid.builder()
                .bidId(0L)
                .price(120D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();


        bid1 = Bid.builder()
                .bidId(1L)
                .price(120D)
                .direction(Direction.SELL)
                .quantity(200D)
                .build();

        resolveResult = comp.resolveInterPoint(Collect.asList(bid0), Collect.asList(bid1));
        Pair<Double, Double> interPoint = resolveResult.getInterPoint();
        Assertions.assertNotNull(interPoint);
        Assertions.assertEquals(interPoint.getLeft(), 100D);
        Assertions.assertEquals(interPoint.getRight(), 120D);


        bid0 = Bid.builder()
                .bidId(0L)
                .price(120D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();


        bid1 = Bid.builder()
                .bidId(1L)
                .price(100D)
                .direction(Direction.SELL)
                .quantity(200D)
                .build();

        resolveResult = comp.resolveInterPoint(Collect.asList(bid0), Collect.asList(bid1));
        interPoint = resolveResult.getInterPoint();

        Assertions.assertNotNull(interPoint);
        Assertions.assertEquals(interPoint.getLeft(), 100D);
        Assertions.assertEquals(interPoint.getRight(), 100D);

        bid0 = Bid.builder()
                .bidId(0L)
                .price(140D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();


        bid1 = Bid.builder()
                .bidId(1L)
                .price(140D)
                .direction(Direction.SELL)
                .quantity(100D)
                .build();


        resolveResult = comp.resolveInterPoint(Collect.asList(bid0), Collect.asList(bid1));
        interPoint = resolveResult.getInterPoint();

        Assertions.assertNotNull(interPoint);
        Assertions.assertEquals(interPoint.getLeft(), 100D);
        Assertions.assertEquals(interPoint.getRight(), 140D);



        bid0 = Bid.builder()
                .bidId(0L)
                .price(200D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();

        bid1 = Bid.builder()
                .bidId(1L)
                .price(100D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();



        Bid bid2 = Bid.builder()
                .bidId(2L)
                .price(50D)
                .direction(Direction.SELL)
                .quantity(50D)
                .build();

        Bid bid3 = Bid.builder()
                .bidId(3L)
                .price(75D)
                .direction(Direction.SELL)
                .quantity(100D)
                .build();

        Bid bid4 = Bid.builder()
                .bidId(3L)
                .price(300D)
                .direction(Direction.SELL)
                .quantity(100D)
                .build();


        resolveResult = comp.resolveInterPoint(Collect.asList(bid0, bid1), Collect.asList(bid2, bid3, bid4));
        interPoint = resolveResult.getInterPoint();

        Assertions.assertNotNull(interPoint);
        Assertions.assertEquals(interPoint.getLeft(), 150D);
        Assertions.assertEquals(interPoint.getRight(), 100D);


        bid0 = Bid.builder()
                .bidId(0L)
                .price(200D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();

        bid1 = Bid.builder()
                .bidId(1L)
                .price(100D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();



        bid2 = Bid.builder()
                .bidId(2L)
                .price(50D)
                .direction(Direction.SELL)
                .quantity(100D)
                .build();

        bid3 = Bid.builder()
                .bidId(3L)
                .price(150D)
                .direction(Direction.SELL)
                .quantity(100D)
                .build();



        resolveResult = comp.resolveInterPoint(Collect.asList(bid0, bid1), Collect.asList(bid2, bid3));
        interPoint = resolveResult.getInterPoint();

        Assertions.assertNotNull(interPoint);
        Assertions.assertEquals(interPoint.getLeft(), 100D);
        Assertions.assertEquals(interPoint.getRight(), 125D);



        bid0 = Bid.builder()
                .bidId(0L)
                .price(200D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();

        bid1 = Bid.builder()
                .bidId(1L)
                .price(100D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();



        bid2 = Bid.builder()
                .bidId(2L)
                .price(50D)
                .direction(Direction.SELL)
                .quantity(150D)
                .build();

        bid3 = Bid.builder()
                .bidId(3L)
                .price(100D)
                .direction(Direction.SELL)
                .quantity(100D)
                .build();



        resolveResult = comp.resolveInterPoint(Collect.asList(bid0, bid1), Collect.asList(bid2, bid3));
        interPoint = resolveResult.getInterPoint();

        Assertions.assertNotNull(interPoint);
        Assertions.assertEquals(interPoint.getLeft(), 200D);
        Assertions.assertEquals(interPoint.getRight(), 100D);


    }

}
