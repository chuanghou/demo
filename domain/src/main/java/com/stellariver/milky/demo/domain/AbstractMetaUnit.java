package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractMetaUnit {

    String metaUnitId;
    String name;
    Province province;
    UnitType unitType;
    Long sourceId;
    Map<TimeFrame, Map<Direction, Double>> capacity;

}
