package com.stellariver.milky.demo.adapter.po;

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

}
