package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("demo_meta_unit")
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetaUnitDO {


    @TableId(type = IdType.INPUT)
    Integer metaUnitId;
    Integer nodeId;
    String name;
    String province;
    String unitType;
    Integer sourceId;
    String capacity;
    String generatorType; // only uniType is generator, this field is not null

}
