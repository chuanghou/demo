package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.PriceLimit;
import com.stellariver.milky.demo.common.Status;
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

        MarketType marketType;

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
    public static class RealtimeBid extends Command {

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
