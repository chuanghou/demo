package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetaUnit {

    String id;
    String name;
    UnitType unitType;
    Map<TimeFrame, Double> quantities;

}
