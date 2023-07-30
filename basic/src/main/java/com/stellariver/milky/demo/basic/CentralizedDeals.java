package com.stellariver.milky.demo.basic;

import com.stellariver.milky.demo.common.Deal;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CentralizedDeals {

    Double buyBidQuantityTotal;
    Double sellBidQuantityTotal;
    Double dealQuantityTotal;
    Double dealAveragePrice;
    List<PointLine> buyPointLines;
    List<PointLine> sellPointLines;
    Pair<Double, Double> interPoint;
    List<Deal> deals;

}
