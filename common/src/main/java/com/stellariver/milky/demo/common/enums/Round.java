package com.stellariver.milky.demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Round {

    ONE(1),
    TWO(2),
    THREE(3);

    final private Integer dbCode;
}
