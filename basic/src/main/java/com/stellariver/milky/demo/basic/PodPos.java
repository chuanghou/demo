package com.stellariver.milky.demo.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PodPos {

    TRANSFER("输电"), RECEIVE("受电");

    final String desc;
}
