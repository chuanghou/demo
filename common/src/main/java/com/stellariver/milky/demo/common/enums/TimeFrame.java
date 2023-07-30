package com.stellariver.milky.demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeFrame {

    PEAK("峰时段"), FLAT("平时段"), VALLEY(" 谷时段");

    final String desc;
}
