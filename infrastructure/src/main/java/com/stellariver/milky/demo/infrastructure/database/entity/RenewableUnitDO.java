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
@TableName("renewable_unit_forecast")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RenewableUnitDO {

    Integer unitId;

    Integer prd;

    @TableField("annual_o_forecast")
    Double annualForecast;

    @TableField("monthly_o_forecast")
    Double monthlyForecast;

    @TableField("da_p_forecast")
    Double daForecast;

}
