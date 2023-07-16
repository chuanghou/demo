package com.stellariver.milky.demo.infrastructure.database.entity;

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

    Integer loadId;
    String loadName;
    Integer prov;
    Integer isMarketLoad;
    Integer nodeId;
}
