package com.stellariver.milky.demo.basic;

import com.stellariver.milky.demo.common.enums.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {

    TRANSFER("输电") {
        @Override
        public Direction interProvincial() {
            return Direction.SELL;
        }
    }, RECEIVE("受电") {
        @Override
        public Direction interProvincial() {
            return Direction.BUY;
        }
    };

    final String desc;

    abstract public Direction interProvincial();
}
