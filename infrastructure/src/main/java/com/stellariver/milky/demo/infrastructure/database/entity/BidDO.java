package com.stellariver.milky.demo.infrastructure.database.entity;

import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.enums.BidStatus;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BidDO {

    Long id;
    Long unitId;
    String province;
    String timeFrame;
    String marketType;
    String direction;
    Double quantity;
    Double price;
    Date date;
    String deals;

    String bidStatus;

}
