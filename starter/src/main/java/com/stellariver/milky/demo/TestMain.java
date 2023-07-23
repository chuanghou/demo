package com.stellariver.milky.demo;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.IntStream;

public class TestMain {
    static Map<Integer, Pair<Integer, Integer>> roundOneMap = Collect.asMap(
            1, Pair.of(1, 6),
            2, Pair.of(2, 4),
            3, Pair.of(3, 5)
    );

    static Map<Integer, Pair<Integer, Integer>> roundTwoMap = Collect.asMap(
            1, Pair.of(2, 4),
            2, Pair.of(3, 5),
            3, Pair.of(1, 6)
    );

    static Map<Integer, Pair<Integer, Integer>> roundThreeMap = Collect.asMap(
            1, Pair.of(3, 5),
            2, Pair.of(1, 6),
            3, Pair.of(2, 4)
    );

    static Map<Integer, Map<Integer, Pair<Integer, Integer>>> alloacteMap = Collect.asMap(
            1, roundOneMap,
            2, roundTwoMap,
            3, roundThreeMap
    );

    private static Pair<Integer, Integer> allocate(Integer roundId, Integer userId, Integer userCount, Integer unitCount) {
        int groupMemberCount = (userCount / 3) + (((userCount % 3) == 0) ? 0 : 1);
        Map<Integer, Pair<Integer, Integer>> integerPairMap = alloacteMap.get(roundId);
        int groupNumber = userId / groupMemberCount + (((userId % groupMemberCount) == 0) ? 0 : 1);
        Pair<Integer, Integer> pair = integerPairMap.get(groupNumber);
        int i = ((userId - 1) % groupMemberCount) + 1;
        int k = unitCount / 6;
        return Pair.of((pair.getLeft() - 1) * k + i, (pair.getRight() - 1) * k + i);
    }

    public static void main(String[] args) {
        ArrayList<Al> als = new ArrayList<>();
        IntStream.range(1, 5 + 1).forEach(agentId -> IntStream.range(1, 4).forEach(roundId -> {
            Pair<Integer, Integer> allocateIds = allocate(roundId, agentId, 5, 30);
            Al build = Al.builder().roundId(roundId).userId(agentId).id0(allocateIds.getLeft()).id1(allocateIds.getRight()).build();
            als.add(build);
        }));
        System.out.println(Json.toJson(als));

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static public class Al{
        Integer roundId;
        Integer userId;
        Integer id0;
        Integer id1;
    }
}
