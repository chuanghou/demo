package com.stellariver.milky.demo.common;

import com.stellariver.milky.demo.common.enums.BidStatus;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bid {

    Long id;
    Long unitId;
    Province province;
    TimeFrame timeFrame;
    MarketType marketType;
    Direction direction;
    Double quantity;
    Double price;
    Date date;
    List<Deal> deals;

    BidStatus bidStatus;

}
