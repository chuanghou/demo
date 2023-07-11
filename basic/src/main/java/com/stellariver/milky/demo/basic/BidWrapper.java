package com.stellariver.milky.demo.basic;

import com.stellariver.milky.demo.common.enums.Bid;
import com.stellariver.milky.demo.common.enums.TxGroup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BidWrapper extends Bid {

    TxGroup txGroup;

    public static BidWrapper wrapper(Bid bid, TxGroup txGroup) {
        return BidWrapper.builder()
                .txGroup(txGroup)
                .direction(bid.getDirection())
                .price(bid.getPrice())
                .quantity(bid.getQuantity())
                .build();
    }

}
