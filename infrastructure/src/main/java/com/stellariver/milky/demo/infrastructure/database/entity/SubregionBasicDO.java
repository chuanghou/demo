package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("subregion_basic")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubregionBasicDO {
    Integer subregionId;
    String subregionName;
    Integer prov;
}
