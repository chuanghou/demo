package com.stellariver.milky.demo.basic;

public interface BasicConvertor {

    default UnitIdentify toUnitIdentify(String unitId) {
        return UnitIdentify.resolve(unitId);
    }

    default String fromUnitIdentify(UnitIdentify unitIdentify) {
        return unitIdentify.getUnitId();
    }

}
