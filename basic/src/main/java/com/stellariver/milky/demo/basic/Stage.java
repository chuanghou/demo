package com.stellariver.milky.demo.basic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Stage {

    Initializing("本次竞赛尚未开赛", "STAGE_ONE_RUNNING", false),
    STAGE_ONE_RUNNING("第一轮，年度省间交易中", "STAGE_ONE_SETTLE", true),
    STAGE_ONE_SETTLE("第一轮，年度省间交易结算", "STAGE_TWO_RUNNING", false),
    STAGE_TWO_RUNNING("第二轮，年度省内交易中", "STAGE_TWO_SETTLE", true),
    STAGE_TWO_SETTLE("第二轮，年度省内交易结算", "STAGE_THREE_RUNNING", false),
    STAGE_THREE_RUNNING("第三轮，月度省间交易中", "STAGE_THREE_SETTLE", true),
    STAGE_THREE_SETTLE("第三轮，月度省间交易结算", "STAGE_FOUR_RUNNING", false),
    STAGE_FOUR_RUNNING("第四轮，月度省间交易中", "STAGE_FOUR_SETTLE", true),
    STAGE_FOUR_SETTLE("第四轮，月度省间交易结算", "END", false),
    END("结束", null, null);

    final String desc;

    final String nextStage;

    final Boolean autoForNext;
}
