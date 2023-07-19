package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("individual_load_forecast")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoadForecastDO {

    Integer loadId;
    Integer prd;
    @TableId("annual_p_forecast")
    Double annualForecast;
    @TableId("monthly_p_forecast")
    Double monthlyForecast;
    @TableField("da_p_forecast")
    Double daForecast;

}
