package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tie_line_power")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TieLinePowerDO {

  @TableId(type = IdType.INPUT)
  Long id;
  Integer roundId;
  String dt;
  Integer prd;
  Double annualTielinePower;
  Double monthlyTielinePower;
  Double daTielinePower;
  Double annualMarketTielinePower;
  Double monthlyMarketTielinePower;
  Double daMarketTielinePower;
  Double annualNonmarketTielinePower;
  Double monthlyNonmarketTielinePower;
  Double daNonmarketTielinePower;
  Boolean isInterprovincialSpotTransaction;
  Integer tielineId;

}
