package com.stellariver.milky.demo.adapter.po;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.adapter.vo.DaBidVO;
import com.stellariver.milky.demo.common.ForecastDaBid;
import com.stellariver.milky.demo.common.NormalDaBid;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DaBidPO {

    Long unitId;

    List<NormalDaBid> normalDaBids;

    List<Double> forecastDaBids;


    public static void main(String[] args) {
        NormalDaBid normalDaBid = NormalDaBid.builder().price(100D).left(100D).right(100D).build();
        DaBidPO build = DaBidPO.builder().unitId(1L)
                .normalDaBids(Collect.asList(normalDaBid, normalDaBid)).forecastDaBids(Collect.asList(100D, 100D)).build();
        System.out.println(Json.toJson(build));
    }

}
