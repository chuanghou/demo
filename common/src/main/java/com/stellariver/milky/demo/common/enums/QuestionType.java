package com.stellariver.milky.demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionType {

    SINGLE(1),
    JUDGE(2),
    MULTI(3);

    final Integer dbCode;

}
