package com.stellariver.milky.demo.adapter.po;

import com.stellariver.milky.common.tool.util.Json;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionPO {

    Long unitId;
    Double left;
    Double right;
    Double ratio;

    public static void main(String[] args) {
        SectionPO build = SectionPO.builder().left(100D).right(200D).ratio(1.1D).unitId(1000L).build();
        System.out.println(Json.toJson(build));
    }

}
