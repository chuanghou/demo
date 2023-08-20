package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("thermal_unit_minoutput_and_startup_shutdown_cost")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartupShutdownCostDO {

  Long id;
  String dt;
  Integer prd;
  Double spotCostMinoutput;
  Double shutdownCurveCost;
  Double startupCurveCost;
  @TableId(type = IdType.INPUT)
  Integer unitId;

}
