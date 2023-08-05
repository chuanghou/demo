package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("market_setting")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarketSettingDO {

    @TableId(type = IdType.INPUT)
    Integer marketSettingId;
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
    Double offerPriceCap;
    Double offerPriceFloor;
    Double bidPriceCap;
    Double bidPriceFloor;
//    String balance_constraint_penalty_factor;
//    String branch_constraint_penalty_factor;
//    String section_constraint_penalty_factor;
    Double loadAnnualMaxForecastErr;
    Double loadMonthlyMaxForecastErr;
    Double loadDaMaxForecastErr;
    Double renewableAnnualMaxForecastErr;
    Double renewableMonthlyMaxForecastErr;
    Double renewableDaMaxForecastErr;
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
    Integer roundId;
//    String round_num;
//    String market_type;
    Integer intraprovincialAnnualBidDuration;
    Integer intraprovincialMonthlyBidDuration;
    Integer intraprovincialSpotBidDuration;
    Integer interprovincialAnnualBidDuration;
    Integer interprovincialMonthlyBidDuration;
    Integer interprovincialSpotBidDuration;
//    String dt;
//    String max_load_coe_send;
//    String min_load_coe_send;
//    String max_load_coe_receive;
//    String min_load_coe_receive;
//    String max_wind_coe_send;
//    String min_wind_coe_send;
//    String max_wind_coe_receive;
//    String min_wind_coe_receive;
    Double transmissionAndDistributionTariff;
    Double regulatedUserTariff;
    Double regulatedProducerPrice;
    Double regulatedInterprovTransmissionPrice;
//    String interprov_trading_mode;
//    String interprov_clearing_mode;
//    String is_setting_default_offer_for_traders;
//    String paper_id;
    Integer intraprovincialAnnualResultDuration;
    Integer intraprovincialMonthlyResultDuration;
    Integer intraprovincialSpotResultDuration;
    Integer interprovincialAnnualResultDuration;
    Integer interprovincialMonthlyResultDuration;
    Integer interprovincialSpotResultDuration;
    Integer settleResultDuration;




}
