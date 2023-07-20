package com.stellariver.milky.demo.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Label {

    transfer_96_analysis("送电省96时段供需分析"),
    receiver_96_analysis("受电省96时段供需分析"),
    inter_provincial_linking("省间联络线图"),

    min_thermal_mw("全省火电最小"),
    annual_renewable_forecast("全省新能源预测"),
    adjustable_thermal_mw("全省火电可调"),
    annual_load_forecast("全省负荷预测"),

    annual_transfer_forecast_mw("全省送电预测"),
    annual_receive_forecast_mw("全省受电预测"),

    receive_target_lower_limit("受电目标上限"),
    receive_target_upper_limit("受电目标下限"),

    blockLoadForecast("该阻塞区负荷预测"),
    blockRenewableForecast("该阻塞区新能源发电预测"),

    maxPs("该机组最大发电能力"),

    generatorForecast("该机组发电预测"),

    baseContractMws("该机组基数合同电量"),

    loadForecast("该负荷用电预测");

    final String desc;
}
