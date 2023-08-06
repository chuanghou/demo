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
@TableName("unit_basic")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneratorDO {

    @TableId(type = IdType.AUTO)
    Integer unitId;
    String unitName;
    Integer prov;
    Integer type;
//    String rated_volt;
//    String rated_capacity;
//    String inplant_use_factor;
    Double minOffDuration;
    Double minOnDuration;
    Double rampUpRate;
    Double rampDnRate;
//    String cold_startup_cost;
//    String warm_startup_cost;
//    String hot_startup_cost;
    Double hotStartupTime;
    Double warmStartupTime;
//    String t_to_MWh;
//    String max_agc;
//    String min_agc;
//    String max_spin_res;
//    String unit_res_factor;
    Double maxP;
    Double minP;
//    String max_q;
//    String min_q;
    @TableField("startup_curve_1")
    Double startupCurve1;
    @TableField("startup_curve_2")
    Double startupCurve2;
    @TableField("startup_curve_3")
    Double startupCurve3;
    @TableField("startup_curve_4")
    Double startupCurve4;
    @TableField("startup_curve_5")
    Double startupCurve5;
    @TableField("startup_curve_6")
    Double startupCurve6;
    @TableField("shutdown_curve_1")
    Double shutdownCurve1;
    @TableField("shutdown_curve_2")
    Double shutdownCurve2;
    @TableField("shutdown_curve_3")
    Double shutdownCurve3;
    @TableField("shutdown_curve_4")
    Double shutdownCurve4;
    @TableField("shutdown_curve_5")
    Double shutdownCurve5;
    @TableField("shutdown_curve_6")
    Double shutdownCurve6;
    Integer numStartupCurvePrds;
    Integer numShutdownCurvePrds;
    Integer nodeId;
//    String plant_id;
//    String unitgroup_id;

}
