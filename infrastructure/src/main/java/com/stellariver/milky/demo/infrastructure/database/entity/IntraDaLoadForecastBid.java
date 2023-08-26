package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("intraprovincial_spot_load_bid")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntraDaLoadForecastBid {

  Long id;
  Integer roundId;
  String dt;
  Integer prd;
  Integer bidId;
  Double bidMw;
  Double bidPrice;
  Integer loadId;

}
