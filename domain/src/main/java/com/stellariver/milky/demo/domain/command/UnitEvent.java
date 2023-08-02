package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.domain.support.event.Event;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

public class UnitEvent {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CentralizedBidden extends Event {

        String unitId;
        List<Bid> bids;

        @Override
        public String getAggregateId() {
            return unitId;
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CentralizedTriggered extends Event {

        Long unitId;
        Long compId;
        MarketType marketType;
        List<Bid> bids;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RtBidDeclared extends Event {

        Long unitId;
        Bid bid;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RtCancelBidDeclared extends Event {

        Long unitId;
        Long compId;
        Bid bid;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }

    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Created extends Event {

        Long unitId;
        Unit unit;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }

    }

}
