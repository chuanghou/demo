package com.stellariver.milky.demo.basic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.common.*;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.TimeFrame;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public interface BasicConvertor {

    default List<Agent> toAgents(String value) {
        return Json.parseList(value, Agent.class);
    }

    default String fromAgents(List<Agent> agents) {
        return Json.toJson(agents);
    }

    default String fromBalanceQuantities(Map<TimeFrame, Map<Direction, Double>> balanceQuantities) {
        return Json.toJson(balanceQuantities);
    }

    default Map<TimeFrame, Map<Direction, Double>> toBalanceQuantities(String balanceQuantities) {
        TypeReference<Map<TimeFrame, Map<Direction, Double>>> typeReference = new TypeReference<Map<TimeFrame, Map<Direction, Double>>>() {};
        return Json.parse(balanceQuantities, typeReference);
    }

    default String fromBids(Map<MarketType, List<Bid>> bids) {
        return Json.toJson(bids);
    }

    default Map<MarketType, List<Bid>> toBids(String bids) {
        TypeReference<Map<MarketType, List<Bid>>> typeReference = new TypeReference<Map<MarketType, List<Bid>>>() {};
        return Json.parse(bids, typeReference);
    }

    default String fromPriceLimit(PriceLimit priceLimit) {
        return Json.toJson(priceLimit);
    }

    default PriceLimit toPriceLimit(String value) {
        return Json.parse(value, PriceLimit.class);
    }

    default Map<MarketType, Map<TimeFrame, GridLimit>> toTransLimit(String value) {
        return Json.parse(value, new TypeReference<Map<MarketType, Map<TimeFrame, GridLimit>>>() {});
    }

    default String fromTransLimit(Map<MarketType, Map<TimeFrame, GridLimit>> durations) {
        return Json.toJson(durations);
    }

    default List<Map<MarketType, Duration>> toDurations(String value) {
        return Json.parse(value, new TypeReference<List<Map<MarketType, Duration>> >() {});
    }

    default String fromDurations(List<Map<MarketType, Duration>>  durations) {
        return Json.toJson(durations);
    }

    default List<Map<MarketType,Map<TimeFrame, Double>>> toReplenishes(String value) {
        return Json.parse(value, new TypeReference<List<Map<MarketType,Map<TimeFrame, Double>>>>() {});
    }

    default String fromReplenishes(List<Map<MarketType, Map<TimeFrame, Double>>> replenishes) {
        return Json.toJson(replenishes);
    }

    default List<ListMultimap<MarketType, Bid>> toCentralizedBids0(String value) {
        return Json.parse(value, new TypeReference<List<ListMultimap<MarketType, Bid>>>() {});
    }

    default String fromCentralizedBids0(List<ListMultimap<MarketType, Bid>> centralizedBids) {
        return Json.toJson(centralizedBids);
    }


    default ListMultimap<MarketType, Bid> toCentralizedBids1(String value) {
        return Json.parse(value, new TypeReference<ArrayListMultimap<MarketType, Bid>>() {});
    }

    default String fromCentralizedBids1(ListMultimap<MarketType, Bid> centralizedBids) {
        return Json.toJson(centralizedBids);
    }

    default Map<TimeFrame, Direction> toDirections(String value) {
        return Json.parse(value, new TypeReference<Map<TimeFrame, Direction>>() {});
    }

    default String fromDirections(Map<TimeFrame, Direction> directions) {
        return Json.toJson(directions);
    }


    default List<Deal> toDeals(String value) {
        return Json.parseList(value, Deal.class);
    }

    default String fromDeals(List<Deal> deals) {
        return Json.toJson(deals);
    }

}
