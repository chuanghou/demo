package com.stellariver.milky.demo.infrastructure.database.entity;

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
@TableName("thermal_unit_cost_histogram")
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlockThermalUnitDO {
    Long id;
    Integer prd;
    Integer costSegId;
    Double costLeftInterval;
    Double costRightInterval;
    Double thermalMw;
    Integer subregionId;

}
