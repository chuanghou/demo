package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("intraprovincial_spot_unit_offer")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntraDaUO {

  Long id;
  Integer roundId;
  String dt;
  Integer prd;
  Integer offerId;
  Double offerMw;
  Double offerPrice;
  Integer unitId;

}
