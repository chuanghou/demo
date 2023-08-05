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
@TableName("individual_load_basic")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoadDO {

    @TableId(type = IdType.AUTO)
    Integer load_id;
    String load_name;
    Integer prv;
    Double max_p;
    Integer is_market_load;
    Integer node_id;
}
