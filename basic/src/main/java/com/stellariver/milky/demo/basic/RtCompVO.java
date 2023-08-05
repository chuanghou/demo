package com.stellariver.milky.demo.basic;

import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.enums.NewBid;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RtCompVO {

    Double currentPrice;
    List<MarketAsk> buyMarketAsks;
    List<MarketAsk> sellMarketAsks;
    List<Deal> deals;
    List<NewBid> buyBids;
    List<NewBid> sellBids;


}
