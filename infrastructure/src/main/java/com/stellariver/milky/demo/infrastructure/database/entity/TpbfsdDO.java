package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tieline_power_band_for_stack_diagram")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TpbfsdDO {
    Long id;
    Integer roundId;
    Date dt;
    Integer prd;
    Integer prov;
    Double maxAnnualReceivingMw;
    Double maxMonthlyReceivingMw;
    Double minAnnualReceivingMw;
    Double minMonthlyReceivingMw;
    Double annualReceivingForecastMw;
    Double monthlyReceivingForecastMw;
    Double intraprovincialAnnualTielinePower;
    Double intraprovincialMonthlyTielinePower;
    Double daReceivingTarget;
    Double daReceivingForecastMw;
}

