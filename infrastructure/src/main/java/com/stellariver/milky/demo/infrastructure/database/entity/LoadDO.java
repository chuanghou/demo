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
    @TableField("load_id")
    Integer loadId;
    @TableField("load_name")
    String loadName;
    @TableField("prv")
    Integer prv;
    @TableField("max_p")
    Double maxP;
    @TableField("is_market_load")
    Integer isMarketLoad;
    @TableField("node_id")
    Integer nodeId;
}
