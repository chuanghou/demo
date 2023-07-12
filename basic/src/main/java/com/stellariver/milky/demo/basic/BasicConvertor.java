package com.stellariver.milky.demo.basic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.common.Agent;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Order;
import com.stellariver.milky.demo.common.Stage;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.TimeFrame;

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

    default String toCentralizedBids(Map<TimeFrame, List<Bid>> centralizedBids) {
        return Json.toJson(centralizedBids);
    }

    default Map<TimeFrame, List<Bid>> toCentralizedBids(String centralizedBids) {
        TypeReference<Map<TimeFrame, List<Bid>>> typeReference = new TypeReference<Map<TimeFrame, List<Bid>>>() {};
        return Json.parse(centralizedBids, typeReference);
    }

    default String fromOrders(Map<String, Order> orders) {
        return Json.toJson(orders);
    }

    default Map<String, Order> toOrders(String orders) {
        TypeReference<Map<String, Order>> typeReference = new TypeReference<Map<String, Order>>() {};
        return Json.parse(orders, typeReference);
    }

    default String fromBids(Map<Stage, List<Bid>> bids) {
        return Json.toJson(bids);
    }

    default Map<Stage, List<Bid>> toBids(String bids) {
        TypeReference<Map<Stage, List<Bid>>> typeReference = new TypeReference<Map<Stage, List<Bid>>>() {};
        return Json.parse(bids, typeReference);
    }

}
