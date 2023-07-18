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
@TableName("unit_basic")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneratorDO {

    @TableId(type = IdType.AUTO)
    Integer unitId;
    String unitName;
    Integer prov;
    Integer type;

}
