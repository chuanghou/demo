package com.stellariver.milky.demo.common;

public class Status {

    public enum MarketStatus {
        OPEN {
            @Override
            public MarketStatus opposite() {
                return CLOSE;
            }
        },
        CLOSE {
            @Override
            public MarketStatus opposite() {
                return OPEN;
            }
        };

        abstract public MarketStatus opposite();
    }

    public enum CompStatus {
        INIT,
        OPEN,
        END
    }

}
