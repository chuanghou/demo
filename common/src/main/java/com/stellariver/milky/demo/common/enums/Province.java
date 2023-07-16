package com.stellariver.milky.demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Province {

    TRANSFER(1, "送电省"),
    RECEIVER(1, "受电省");

    final Integer dbCode;
    final String desc;

}
