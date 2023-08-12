package com.stellariver.milky.demo.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Label {


    offer_price_cap("机组申报价格上限（元/MWh）"),
    offer_price_floor("机组申报价格下限（元/MWh）"),
    bid_price_cap("用户申报价格上限（元/MWh）"),
    bid_price_floor("用户申报价格下限（元/MWh）"),


    load_annual_max_forecast_err("负荷年度预测百分比误差"),
    load_monthly_max_forecast_err("负荷月度预测百分比误差"),
    load_da_max_forecast_err("负荷日前预测百分比误差"),
    renewable_annual_max_forecast_err("新能源年度预测百分比误差"),
    renewable_monthly_max_forecast_err("新能源月度预测百分比误差"),
    renewable_da_max_forecast_err("新能源日前预测百分比误差"),

    transmission_and_distribution_tariff("输配电价（元/MWh）"),
    regulated_user_tariff("保障性用户电价（元/MWh）"),
    regulated_producer_price("基数电量电价（元/MWh）"),
    regulated_interprov_transmission_price("省间交易的政府定价（元/MWh）"),

    round_id("当前比赛轮次"),
    interprov_clearing_mode("省间交易出清方式"),
    interprov_trading_mode("省间交易组织形式"),

    sender_peak_prds("送电省峰时段-现货时段"),
    sender_flat_prds("送电省平时段-现货时段"),
    sender_valley_prds("送电谷峰时段-现货时段"),

    receive_peak_prds("受电省峰时段-现货时段"),
    receive_flat_prds("受电省平时段-现货时段"),
    receive_valley_prds("受电省谷时段-现货时段"),

    unit_name("机组名称"),
    prov_of_generator("机组所在省份"),
    node_id_of_generator("机组所在节点"),
    min_off_duration("最小停机时间（h）"),
    min_on_duration("最小运行时间（h）"),
    ramp_up_rate("爬坡速率（MW/min）"),
    ramp_dn_rate("滑坡速率（MW/min）"),
    hot_startup_time("自关机起热启动的时间上限（h）"),
    warm_startup_time("自关机起温启动的时间上限（h）"),
    max_p_of_classic_generator("最大技术出力（MW）"),
    min_p_of_classic_generator("最小技术出力（MW）"),
    startup_curve_x	("开机过程第X段典型出力（MW）"),
    shutdown_curve_x("停机过程第X段典型出力（MW）"),
    num_startup_curve_prds("开机曲线实际时段数"),
    num_shutdown_curve_prds	("停机曲线实际时段数"),

    max_p_of_renewable("发电容量（MW）"),


    load_name("负荷名称"),
    prov_of_load("负荷所在省份"),
    node_id_of_load("负荷所在节点"),

    max_p_of_load("配电电容量（MW）"),


    market_profile_locate_province("所在省份"),
    market_profile_generator("机组"),
    market_profile_load("负荷(考虑省间输电)"),
    market_profile_offer_require_ratio("供需比"),

    transfer_96_analysis("送电省24时段供需分析"),
    receiver_96_analysis("受电省24时段供需分析"),
    inter_provincial_linking("省间联络线图"),

    min_thermal_mw("全省火电最小"),
    annual_renewable_forecast("全省新能源预测"),
    adjustable_thermal_mw("全省火电可调"),
    annual_load_forecast("全省负荷预测"),

    transfer_forecast_mw("全省送电预测"),
    receive_forecast_mw("全省受电预测"),

    receive_target_upper_limit("受电目标上限"),
    receive_target_lower_limit("受电目标下限"),
    intraprovincial_annual_tieline_power("省间年度已成交对应"),
    da_receiving_target("受电目标"),
    intraprovincial_monthly_tieline_power("省间年度月度已成交"),

    blockLoadForecast("该阻塞区负荷预测"),
    blockRenewableForecast("该阻塞区新能源发电预测"),


    maxPs("该机组最大发电能力"),             // classic
    generatorForecast("该机组发电预测"),     // renewable;

    generatorInventoryUpperLimit("该机组持仓量上限"),


    baseContractMws("该机组基数合同电量"),
    generatorDiffDeals("该机组差价合约已成交"),

    generatorInterProvinceAnnualDeal("该机组省间年度已成交"),
    generatorIntraProvinceAnnualDeal("该机组省内年度已成交"),
    generatorInterProvinceMonthlyDeal("该机组省间月度已成交"),
    generatorIntraProvinceMonthlyDeal("该机组省内月度已成交"),

    loadForecast("该负荷用电预测"),
    loadInventoryUpperLimit("该负荷持仓量上限"),

    loadDiffDeals("该负荷差价合约已成交"),
    loadInterProvinceAnnualDeal("该负荷省间年度已成交"),
    loadIntraProvinceAnnualDeal("该负荷省内年度已成交"),
    loadInterProvinceMonthlyDeal("该负荷省间月度已成交"),
    loadIntraProvinceMonthlyDeal("该负荷省内月度已成交"),

    costOfClassicOfAnnualAndMonthly_basic("最小技术出力成本(元/MWh)"),
    costOfClassicOfAnnualAndMonthly_range("最小技术出力成本之上运行成本区间(元/MWh)"),
    costOfRenewable("补贴(元/MWh)");


    final String desc;
}
