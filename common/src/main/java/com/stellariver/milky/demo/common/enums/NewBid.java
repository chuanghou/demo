package com.stellariver.milky.demo.common.enums;

import com.stellariver.milky.demo.common.MarketType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewBid {
    Long bidId;
    Long unitId;
    Province province;
    TimeFrame timeFrame;
    MarketType marketType;
    Direction direction;
    Double quantity;
    Double price;
    Date date;
}
