package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("individual_load_basic")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoadDO {

    @TableId(type = IdType.AUTO)
    Integer loadId;
    String loadName;
    Integer prov;
    Double maxP;
    Integer isMarketLoad;
    Integer nodeId;
}
