package com.stellariver.milky.demo.adapter.po;

import com.stellariver.milky.demo.common.DaBid;
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

    List<DaBid> daBids;

    List<Double> daForecastBid;

}
