package com.stellariver.milky.demo.common.enums;

import com.stellariver.milky.common.base.ErrorEnumsBase;
import com.stellariver.milky.common.base.SysEx;

public enum Direction {
    BUY {
        @Override
        public Direction opposite() {
            return SELL;
        }
    }, SELL {
        @Override
        public Direction opposite() {
            return BUY;
        }
    }, UNKNOWN {
        @Override
        public Direction opposite() {
            throw new SysEx(ErrorEnumsBase.UNREACHABLE_CODE);
        }
    };

    public abstract Direction opposite();
}
