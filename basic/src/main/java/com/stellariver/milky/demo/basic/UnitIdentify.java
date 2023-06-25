package com.stellariver.milky.demo.basic;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitIdentify {

    String compId;
    String podId;
    TimeFrame timeFrame;

    public String getUnitId() {
        return String.format("%s.%s.%s", compId, podId, timeFrame.name());
    }

    static public UnitIdentify resolve(String unitId) {
        String[] split = StringUtils.split(unitId);
        return UnitIdentify.builder()
                .compId(split[0])
                .podId(split[1])
                .timeFrame(TimeFrame.valueOf(split[2]))
                .build();
    }

}
