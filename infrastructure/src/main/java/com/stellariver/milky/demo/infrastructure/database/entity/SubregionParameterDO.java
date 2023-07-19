package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("subregion_parameter_release")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubregionParameterDO {
    Integer subregionId;
    Integer prd;
    Double annualLoadForecast;
    Double monthlyLoadForecast;
    Double daLoadForecast;
    Double annualRenewableForecast;
    Double monthlyRenewableForecast;
    Double daRenewableForecast;
}
