package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("intraprovincial_spot_renewable_unit_forecast")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntraDaGeneratorForecastBid {

  Long id;
  Integer roundId;
  String dt;
  Integer prd;
  Double forecastMw;
  Integer unitId;

}
