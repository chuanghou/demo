package com.stellariver.milky.demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeFrame {

    PEAK(1, "峰时段"), FLAT(2, "平时段"), VALLEY(3, " 谷时段");

    final Integer dbCode;
    final String desc;

}
