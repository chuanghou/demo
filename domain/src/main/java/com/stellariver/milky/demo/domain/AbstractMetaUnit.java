package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractMetaUnit {

    Integer metaUnitId;
    Integer nodeId;
    String name;
    Province province;
    UnitType unitType;
    Integer sourceId;
    Map<TimeFrame, Map<Direction, Double>> capacity;

}
