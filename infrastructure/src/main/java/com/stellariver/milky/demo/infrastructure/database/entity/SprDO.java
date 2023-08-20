package com.stellariver.milky.demo.infrastructure.database.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_parameter_release")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SprDO {
    Long id;
    Date dt;
    Integer prd;
    Integer prov;
    Double minThermalMw;
    Double adjustableThermalMw;
    Double annualLoadForecast;
    Double monthlyLoadForecast;
    Double daLoadForecast;
    Double rtLoad;
    Double annualRenewableForecast;
    Double monthlyRenewableForecast;
    Double daRenewableForecast;
    Double rtRenewable;
    Double resUp;
    Double resDn;
    Double minThermalCapacity;
    Double coalPrice;
    Double renewableGovernmentSubsidy;
}
