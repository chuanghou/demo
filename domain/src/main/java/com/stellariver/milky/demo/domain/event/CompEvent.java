package com.stellariver.milky.demo.domain.event;

import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.domain.support.event.Event;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

public class CompEvent {

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Cleared extends Event {

        Long compId;
        MarketType marketType;
        List<Deal> deals;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }

    }


    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Created extends Event {

        Long compId;
        Comp comp;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Started extends Event {

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
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Stepped extends Event {

        Long compId;

        Integer lastRoundId;
        MarketType lastMarketType;
        Status.MarketStatus lastMarketStatus;
        Integer nextRoundId;
        MarketType nextMarketType;
        Status.MarketStatus nextMarketStatus;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CentralizedBidAccept extends Event {

        Long compId;
        Integer roundId;
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
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class End extends Event {

        Long compId;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }
    }

}
