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

    @Override
    public String toString() {
        return String.format("%s.%s", compId, podId);
    }

    static public UnitIdentify resolve(String unitIdentify) {
        String[] split = StringUtils.split(unitIdentify);
        return UnitIdentify.builder()
                .compId(split[0])
                .podId(split[1])
                .build();
    }

}
