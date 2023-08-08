package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("thermal_unit_operating_cost")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThermalUnitOperatingCost {

  Long id;
  String dt;
  Integer prd;
  Integer spotCostId;
  Double spotCostMarginalCost;
  Double spotCostMw;
  Integer unitId;

}
