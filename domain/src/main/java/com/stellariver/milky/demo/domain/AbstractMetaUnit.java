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

    public static void main(String[] args) {
        HashMap<TimeFrame, Map<Direction, Double>> timeFrameMapHashMap = new HashMap<>();
        HashMap<Direction, Double> directionDoubleHashMap = new HashMap<>();
        directionDoubleHashMap.put(Direction.BUY, 100.00);
        timeFrameMapHashMap.put(TimeFrame.FLAT, directionDoubleHashMap);
        timeFrameMapHashMap.put(TimeFrame.VALLEY, directionDoubleHashMap);
        timeFrameMapHashMap.put(TimeFrame.PEAK, directionDoubleHashMap);

        System.out.println(Json.toJson(timeFrameMapHashMap));

        timeFrameMapHashMap = new HashMap<>();
       directionDoubleHashMap = new HashMap<>();
        directionDoubleHashMap.put(Direction.SELL, 100.00);
        timeFrameMapHashMap.put(TimeFrame.FLAT, directionDoubleHashMap);
        timeFrameMapHashMap.put(TimeFrame.VALLEY, directionDoubleHashMap);
        timeFrameMapHashMap.put(TimeFrame.PEAK, directionDoubleHashMap);

        System.out.println(Json.toJson(timeFrameMapHashMap));

    }
}
