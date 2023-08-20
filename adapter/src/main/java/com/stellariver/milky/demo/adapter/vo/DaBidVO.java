package com.stellariver.milky.demo.adapter.vo;

import com.stellariver.milky.demo.common.DaBid;
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
    
    List<DaBid> daBids;
    List<Triple<Double, Double, Double>> daCostLines;

    List<Double> forecastQuantities;
    List<Double> forecastBids;

}
