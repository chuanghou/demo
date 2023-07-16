package com.stellariver.milky.demo.common.enums;

import com.stellariver.milky.common.base.ErrorEnumsBase;
import com.stellariver.milky.common.base.SysEx;

public enum Direction {
    BUY {

        @Override
        public Direction opposite() {
            return SELL;
        }

        @Override
        public boolean across(double value, double base) {
            return value < base;
        }

    }, SELL {

        @Override
        public Direction opposite() {
            return BUY;
        }

        @Override
        public boolean across(double value, double base) {
            return value > base;
        }

    }, UNKNOWN {

        @Override
        public Direction opposite() {
            throw new SysEx(ErrorEnumsBase.UNREACHABLE_CODE);
        }

        @Override
        public boolean across(double value, double base) {
            throw new SysEx(ErrorEnumsBase.UNREACHABLE_CODE);
        }

    };

    public abstract Direction opposite();

    public abstract boolean across(double value, double base);
}