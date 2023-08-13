package com.stellariver.milky.demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionType {

    SINGLE(1, 5),
    JUDGE(2, 5),
    MULTI(3, 10);

    final Integer dbCode;
    final Integer ratio;

}
