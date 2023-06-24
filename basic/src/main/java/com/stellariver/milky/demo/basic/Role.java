package com.stellariver.milky.demo.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("管理员"),
    COMPETITOR("参赛者");

    final String desc;

}
