package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("thermal_unit_cost_histogram")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThermalUnitCostDO {
    Integer subregionId;
    Integer prd;
    Integer costSegId;
    Double costLeftInterval;
    Double costRightInterval;
    Double thermalMw;
}
