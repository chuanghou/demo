package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractMetaUnit {

    Integer metaUnitId;
    String name;
    Province province;
    UnitType unitType;
    Integer sourceId;
    Map<TimeFrame, Map<Direction, Double>> capacity;

}
