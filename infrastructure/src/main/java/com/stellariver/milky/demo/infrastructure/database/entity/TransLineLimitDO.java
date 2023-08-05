package com.stellariver.milky.demo.infrastructure.database.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("forward_receiving_province_unmet_demand")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransLineLimitDO {

    Integer pfvPrd;
    Double minAnnualReceivingMw;
    Double minMonthlyReceivingMw;
    Double maxAnnualReceivingMw;
    Double maxMonthlyReceivingMw;

}
