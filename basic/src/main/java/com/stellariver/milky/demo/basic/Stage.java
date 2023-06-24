package com.stellariver.milky.demo.basic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Stage {

    Initializing("本次竞赛尚未开赛"),
    STAGE_ONE_RUNNING("第一轮，年度省间交易中"),
    STAGE_ONE_SETTLE("第一轮，年度省间交易结算"),
    STAGE_TWO_RUNNING("第二轮，年度省内交易中"),
    STAGE_TWO_SETTLE("第二轮，年度省内交易结算"),
    STAGE_THREE_RUNNING("第三轮，月度省间交易中"),
    STAGE_THREE_SETTLE("第三轮，月度省间交易结算"),
    STAGE_FOUR_RUNNING("第四轮，月度省间交易中"),
    STAGE_FOUR_SETTLE("第四轮，月度省间交易结算"),
    END("结束");

    final String name;
}
