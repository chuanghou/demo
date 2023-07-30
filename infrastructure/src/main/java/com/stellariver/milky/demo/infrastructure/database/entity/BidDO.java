package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@TableName("demo_bid")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BidDO {

    @TableId(type = IdType.INPUT)
    Long bidId;
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
