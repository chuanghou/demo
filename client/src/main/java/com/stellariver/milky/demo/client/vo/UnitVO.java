package com.stellariver.milky.demo.client.vo;

import com.stellariver.milky.demo.common.Bid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitVO {

    String unitId;
    String name;
    String compId;
    String position;
    String unitType;
    String userId;

    Double peakBuyBalance;
    Double peakSellBalance;
    Double flatBuyBalance;
    Double flatSellBalance;
    Double valleyBuyBalance;
    Double valleySellBalance;

    List<Bid> peakBids;
    List<Bid> valleyBids;
    List<Bid> flatBids;

    List<OrderVO> orderVOs;

}
