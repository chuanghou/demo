package com.stellariver.milky.demo.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MarketType {

    INTER_ANNUAL_PROVINCIAL(0, "省间年度"),
    INTRA_ANNUAL_PROVINCIAL(1,"省内年度"),
    INTER_MONTHLY_PROVINCIAL(2, "省间月度"),
    INTRA_MONTHLY_PROVINCIAL(3,"省内月度"),
    INTRA_SPOT_PROVINCIAL(4,"省内现货"),
    INTER_SPOT_PROVINCIAL(5,"省间现货"),
    FINAL_CLEAR(6,"结算");

    final Integer dbCode;
    final String desc;

}
