package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("market_setting")
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarketSettingDO {

    @TableId(type = IdType.INPUT)
    Integer marketSettingId;
    Double offerPriceCap;
    Double offerPriceFloor;
    Double bidPriceCap;
    Double bidPriceFloor;
    Integer roundId;

    Integer interprovincialSpotBidDuration;
    Integer interprovincialAnnualBidDuration;
    Integer interprovincialMonthlyBidDuration;
    Integer intraprovincialSpotBidDuration;
    Integer intraprovincialAnnualBidDuration;
    Integer intraprovincialMonthlyBidDuration;

    Integer interprovincialSpotResultDuration;
    Integer interprovincialAnnualResultDuration;
    Integer interprovincialMonthlyResultDuration;
    Integer intraprovincialSpotResultDuration;
    Integer intraprovincialAnnualResultDuration;
    Integer intraprovincialMonthlyResultDuration;
    Integer settleResultDuration;

}
