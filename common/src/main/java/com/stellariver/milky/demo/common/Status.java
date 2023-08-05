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

    public enum CompStatus {
        INIT,
        OPEN,
        END
    }

}
