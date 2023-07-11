package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.Order;
import com.stellariver.milky.demo.common.TxGroup;
import com.stellariver.milky.domain.support.event.Event;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

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
    public static class RealtimeBidden extends Event {

        String unitId;
        String compId;
        Bid bid;

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
    public static class Cancelled extends Event {

        String unitId;
        Order order;

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
    public static class CancelledReported extends Event {

        String unitId;
        Order order;

        @Override
        public String getAggregateId() {
            return unitId;
        }

    }


    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DealReported extends Event {

        String bidId;
        TxGroup txGroup;
        Deal deal;

        @Override
        public String getAggregateId() {
            return txGroup.getUnitId();
        }

    }

}
