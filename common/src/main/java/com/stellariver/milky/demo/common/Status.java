package com.stellariver.milky.demo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Status {

    @Getter
    @AllArgsConstructor
    public enum MarketStatus {
        OPEN("用户报价"){
            @Override
            public MarketStatus opposite() {
                return CLOSE;
            }
        },
        CLOSE("结果查看") {
            @Override
            public MarketStatus opposite() {
                return OPEN;
            }
        };

        abstract public MarketStatus opposite();

        final String desc;
    }

    @Getter
    @AllArgsConstructor
    public enum CompStatus {

        INIT("初始化"),
        OPEN("开放中"),
        END("已结束");

        final String desc;
    }

}
