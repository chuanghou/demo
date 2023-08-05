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
    @TableField("unit_id")
    Integer unit_id;
    @TableField("unit_name")
    String unit_name;
    @TableField("prov")
    Integer prov;
    @TableField("type")
    Integer type;
//    String rated_volt;
//    String rated_capacity;
//    String inplant_use_factor;
    @TableField("min_off_duration")
    Double min_off_duration;
    @TableField("min_on_duration")
    Double min_on_duration;
    @TableField("ramp_up_rate")
    Double ramp_up_rate;
    @TableField("ramp_dn_rate")
    Double ramp_dn_rate;
//    String cold_startup_cost;
//    String warm_startup_cost;
//    String hot_startup_cost;
    @TableField("hot_startup_time")
    Double hot_startup_time;
    @TableField("warm_startup_time")
    Double warm_startup_time;
//    String t_to_MWh;
//    String max_agc;
//    String min_agc;
//    String max_spin_res;
//    String unit_res_factor;
    @TableField("max_p")
    Double max_p;
    @TableField("min_p")
    Double min_p;
//    String max_q;
//    String min_q;
    @TableField("startup_curve_1")
    Double startup_curve_1;
    @TableField("startup_curve_2")
    Double startup_curve_2;
    @TableField("startup_curve_3")
    Double startup_curve_3;
    @TableField("startup_curve_4")
    Double startup_curve_4;
    @TableField("startup_curve_5")
    Double startup_curve_5;
    @TableField("startup_curve_6")
    Double startup_curve_6;
    @TableField("shutdown_curve_1")
    Double shutdown_curve_1;
    @TableField("shutdown_curve_2")
    Double shutdown_curve_2;
    @TableField("shutdown_curve_3")
    Double shutdown_curve_3;
    @TableField("shutdown_curve_4")
    Double shutdown_curve_4;
    @TableField("shutdown_curve_5")
    Double shutdown_curve_5;
    @TableField("shutdown_curve_6")
    Double shutdown_curve_6;
    @TableField("num_startup_curve_prds")
    Integer num_startup_curve_prds;
    @TableField("num_shutdown_curve_prds")
    Integer num_shutdown_curve_prds;
    @TableField("node_id")
    Integer node_id;
//    String plant_id;
//    String unitgroup_id;

}
