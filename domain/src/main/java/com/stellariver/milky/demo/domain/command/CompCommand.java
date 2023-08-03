package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.GridLimit;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.PriceLimit;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.NewBid;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.domain.support.command.Command;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class CompCommand {

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create extends Command {

        Long compId;
        Integer agentTotal;
        PriceLimit priceLimit;
        Map<MarketType, Map<TimeFrame, GridLimit>> transLimit;
        List<Map<MarketType, Duration>> durations;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Clear extends Command {

        Long compId;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Start extends Command {

        Long compId;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }

    }


    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Edit extends Command {

        Long compId;

        List<Map<MarketType, Duration>> durations;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }

    }


    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Close extends Command {

        Long compId;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Step extends Command {

        Long compId;
        Stage nextStage;


        @Override
        public String getAggregateId() {
            return compId.toString();
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RtNewBid extends Command {

        Long compId;
        NewBid newBid;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RtCancelBid extends Command {

        Long compId;
        Long unitId;
        Long bidId;
        Direction bidDirection;
        Province province;
        TimeFrame timeFrame;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CentralizedBid extends Command {

        Long compId;
        Long unitId;
        MarketType marketType;
        List<Bid> bids;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RtMarketClose extends Command{

        Long compId;
        @Override
        public String getAggregateId() {
            return compId.toString();
        }
    }
}
