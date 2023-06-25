package com.stellariver.milky.demo.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PodType {
    GENERATOR("机组"), LOAD("负荷");

    final String desc;
}
