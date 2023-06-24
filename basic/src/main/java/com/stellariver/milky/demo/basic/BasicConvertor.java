package com.stellariver.milky.demo.basic;

import com.stellariver.milky.common.tool.util.Json;

import java.util.List;

public interface BasicConvertor {

    default UnitIdentify toUnitIdentify(String unitId) {
        return UnitIdentify.resolve(unitId);
    }

    default String fromUnitIdentify(UnitIdentify unitIdentify) {
        return unitIdentify.getUnitId();
    }

    default List<Agent> toAgents(String value) {
        return Json.parseList(value, Agent.class);
    }

    default String fromAgents(List<Agent> agents) {
        return Json.toJson(agents);
    }

}
