package com.stellariver.milky.demo;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.stellariver.milky.demo.basic.TokenUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class TokenTest {

    @Test
    @SuppressWarnings({"beta", "UnstableApiUsage"})
    public void testToken() {
        String test = TokenUtils.sign("test");
        String agentId = TokenUtils.getUserId(test);
        Assertions.assertEquals("test", agentId);

        RangeMap<Double, Pair<Double, Double>> rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closed(1D, 2D), Pair.of(1D, 2D));
        rangeMap.put(Range.singleton(1D), Pair.of(1D, 1D));
        System.out.println(rangeMap.get(1D));
    }

}

