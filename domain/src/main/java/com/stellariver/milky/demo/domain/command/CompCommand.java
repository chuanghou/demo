package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.common.*;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.domain.support.command.Command;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.List;
import java.util.Map;

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
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Step extends Command {

        Long compId;
        Integer targetRoundId;
        MarketType targetMarketType;
        Status.MarketStatus targetMarketStatus;

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
    public static class RtNewBidDeclare extends Command {

        Long compId;
        Long unitId;
        Bid bid;

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
    public static class RtCancelBidDeclare extends Command {

        Long compId;
        Long unitId;
        Long bidId;
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

}
