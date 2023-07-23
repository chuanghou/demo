package com.stellariver.milky.demo.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeneratorType {

    CLASSIC("传统"), RENEWABLE("新能源");

    final String desc;

}
