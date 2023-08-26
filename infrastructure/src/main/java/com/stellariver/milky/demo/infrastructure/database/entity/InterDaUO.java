package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("interprovincial_spot_unit_offer")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InterDaUO {

  Long id;
  Integer roundId;
  String dt;
  Integer prd;
  Double spotOfferMw1;
  Double spotOfferMw2;
  Double spotOfferMw3;
  Double spotOfferPrice1;
  Double spotOfferPrice2;
  Double spotOfferPrice3;
  Integer unitId;

}
