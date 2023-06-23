package com.stellariver.milky.demo.basic;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitIdentify {

    String podId;
    Type type;
    String date;
    TimeFrame timeFrame;

    public String getUnitId() {
        return String.format("%s.%s.%s.%s", podId, type.name(), date, timeFrame.name());
    }

    static public UnitIdentify resolve(String unitId) {
        String[] split = StringUtils.split(unitId);
        return UnitIdentify.builder().podId(split[0])
                .type(Type.valueOf(split[1]))
                .date(split[2])
                .timeFrame(TimeFrame.valueOf(split[3]))
                .build();
    }

}
