package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.basic.Direction;
import com.stellariver.milky.demo.basic.TimeFrame;
import com.stellariver.milky.domain.support.command.Command;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * @author houchuang
 */
public class RealTimeBid {

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static public class Cancel extends Command {

        String transactionId;
        Direction direction;
        Double quantity;
        Double price;

        @Override
        public String getAggregateId() {
            return transactionId;
        }

    }

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static public class Declare extends Command {

        String unitId;
        TimeFrame timeFrame;
        Direction direction;
        Double quantity;
        Double price;

        @Override
        public String getAggregateId() {
            return unitId;
        }

    }


}
