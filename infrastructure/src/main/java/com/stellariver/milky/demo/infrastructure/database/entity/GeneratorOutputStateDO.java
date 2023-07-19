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
@TableName("unit_output_state_setting")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneratorOutputStateDO {

    @TableId(type = IdType.AUTO)
    Integer unitId;
    Integer prd;
    Double baseMw;
}
