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
    Integer load_id;
    @TableField("load_name")
    String load_name;
    @TableField("prv")
    Integer prv;
    @TableField("max_p")
    Double max_p;
    @TableField("is_market_load")
    Integer is_market_load;
    @TableField("node_id")
    Integer node_id;
}
