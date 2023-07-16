package com.stellariver.milky.demo;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.PointLine;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class InterPointTest {

    @Test
    public void testInterPoint() {
        Comp comp = new Comp();
        Bid bid0 = Bid.builder()
                .id("1")
                .price(120D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();


        Bid bid1 = Bid.builder()
                .id("1")
                .price(220D)
                .direction(Direction.SELL)
                .quantity(100D)
                .build();

        Comp.ResolveResult resolveResult = comp.resolveInterPoint(Collect.asList(bid0), Collect.asList(bid1));
        Assertions.assertNull(resolveResult);


        bid0 = Bid.builder()
                .id("1")
                .price(120D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();


        bid1 = Bid.builder()
                .id("1")
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
                .id("1")
                .price(120D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();


        bid1 = Bid.builder()
                .id("1")
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
                .id("1")
                .price(140D)
                .direction(Direction.BUY)
                .quantity(100D)
                .build();


        bid1 = Bid.builder()
                .id("1")
                .price(120D)
                .direction(Direction.SELL)
                .quantity(50D)
                .build();

        Bid bid2 = Bid.builder()
                .id("1")
                .price(320D)
                .direction(Direction.SELL)
                .quantity(200D)
                .build();

        resolveResult = comp.resolveInterPoint(Collect.asList(bid0), Collect.asList(bid1));
        interPoint = resolveResult.getInterPoint();

        Assertions.assertNotNull(interPoint);
        Assertions.assertEquals(interPoint.getLeft(), 50D);
        Assertions.assertEquals(interPoint.getRight(), 140D);

    }
}
