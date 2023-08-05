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
@TableName("tie_line_power")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TieLinePowerDO {
    @TableId(type = IdType.INPUT)
    Long id;
    Integer round_id;
    Integer prd;
    Double annual_nonmarket_tieline_power;
    Double monthly_nonmarket_tieline_power;
    Double da_nonmarket_tieline_power;
}
