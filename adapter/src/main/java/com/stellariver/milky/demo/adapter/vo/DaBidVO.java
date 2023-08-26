package com.stellariver.milky.demo.adapter.vo;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.common.NormalDaBid;
import com.stellariver.milky.demo.common.Section;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DaBidVO {

    Double min;
    Double max;
    
    List<NormalDaBid> normalDaBids;
    List<Section> daCostLines;

    List<Double> forecastQuantities;
    List<Double> forecastBids;

    public static void main(String[] args) {

        Section section = new Section(100D, 100D, 100D);
        NormalDaBid normalDaBid = NormalDaBid.builder().left(100D).right(120D).price(1000D).build();

        DaBidVO build = DaBidVO.builder()
                .min(100D)
                .max(300D)
                .normalDaBids(Collect.asList(normalDaBid, normalDaBid))
                .daCostLines(Collect.asList(section, section))
                .forecastQuantities(Collect.asList(100D, 100D))
                .forecastBids(Collect.asList(100D, 100D))
                .build();
        System.out.println(Json.toJson(build));

    }

}
