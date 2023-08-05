package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("market_setting")
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarketSettingDO {

    @TableId(type = IdType.INPUT)
    Integer market_setting_id;
//    String objective_type;
//    String is_network_loss;
//    String is_inplant_use;
//    String prd_num;
//    String is_startup_cost;
//    String is_ramp_constraint;
//    String is_max_on_off_times_constraint;
//    String is_min_on_off_duration_constraint;
//    String is_branch_constraint;
//    String is_section_constraint;
//    String is_sys_res_constraint;
//    String is_unitgroup_MWh_constraint;
//    String is_unitgroup_MW_constraint;
//    String is_unitgroup_res_constraint;
//    String is_entering_review_stage;
//    String is_conducting_answering_module;
    Double offer_price_cap;
    Double offer_price_floor;
    Double bid_price_cap;
    Double bid_price_floor;
//    String balance_constraint_penalty_factor;
//    String branch_constraint_penalty_factor;
//    String section_constraint_penalty_factor;
    Double load_annual_max_forecast_err;
    Double load_monthly_max_forecast_err;
    Double load_da_max_forecast_err;
    Double renewable_annual_max_forecast_err;
    Double renewable_monthly_max_forecast_err;
    Double renewable_da_max_forecast_err;
//    String forward_num_offer_segs;
//    String forward_num_bid_segs;
//    String spot_num_offer_segs;
//    String spot_num_bid_segs;
//    String thermal_forecast_confidence;
//    String load_forecast_confidence;
//    String renewable_annual_forecast_confidence;
//    String renewable_monthly_forecast_confidence;
//    String renewable_da_forecast_confidence;
//    String max_startup_curve_prds;
//    String max_shutdown_curve_prds;
//    String trader_num;
//    String robot_num;
    Integer round_id;
//    String round_num;
//    String market_type;
    Integer intraprovincial_annual_bid_duration;
    Integer intraprovincial_monthly_bid_duration;
    Integer intraprovincial_spot_bid_duration;
    Integer interprovincial_annual_bid_duration;
    Integer interprovincial_monthly_bid_duration;
    Integer interprovincial_spot_bid_duration;
//    String dt;
//    String max_load_coe_send;
//    String min_load_coe_send;
//    String max_load_coe_receive;
//    String min_load_coe_receive;
//    String max_wind_coe_send;
//    String min_wind_coe_send;
//    String max_wind_coe_receive;
//    String min_wind_coe_receive;
    Double transmission_and_distribution_tariff;
    Double regulated_user_tariff;
    Double regulated_producer_price;
    Double regulated_interprov_transmission_price;
//    String interprov_trading_mode;
//    String interprov_clearing_mode;
//    String is_setting_default_offer_for_traders;
//    String paper_id;
    Integer intraprovincial_annual_result_duration;
    Integer intraprovincial_monthly_result_duration;
    Integer intraprovincial_spot_result_duration;
    Integer interprovincial_annual_result_duration;
    Integer interprovincial_monthly_result_duration;
    Integer interprovincial_spot_result_duration;
    Integer settle_result_duration;




}
