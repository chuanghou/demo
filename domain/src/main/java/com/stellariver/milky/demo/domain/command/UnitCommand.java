package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.common.*;
import com.stellariver.milky.demo.domain.MetaUnit;
import com.stellariver.milky.domain.support.command.Command;
import com.stellariver.milky.domain.support.event.Event;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

public class UnitCommand {

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RealtimeDeal extends Command {

        Long unitId;
        Long bidId;
        Deal deal;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DealReport extends Command {

        Long unitId;
        List<Deal> deals;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CancelReport extends Command {

        Long unitId;
        List<Deal> deals;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }

    }


    /**
     * Yearly Exterior Provincial
     * @author houchuang
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RtNewBidDeclare extends Command {

        Long unitId;

        Bid bid;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }

    }

    /**
     * Yearly Exterior Provincial
     * @author houchuang
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CentralizedBid extends Command {

        Long unitId;

        List<Bid> bids;

        String bidPOs;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }


    }

    /**
     * Yearly Exterior Provincial
     * @author houchuang
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CentralizedTrigger extends Command {

        Long unitId;

        MarketType marketType;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }

    }

    /**
     * @author houchuang
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create extends Command {

        Long unitId;
        Integer userId;
        Long compId;
        Integer roundId;
        MetaUnit metaUnit;


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
    public static class CentralizedBidden extends Event {

        Long unitId;

        List<Bid> bids;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RtCancelBidDeclare extends Command {

        Long unitId;
        Long bidId;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RtBidCancelled extends Command {

        Long unitId;
        Long bidId;
        Double remainder;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DaBidDeclare extends Command {

        Long unitId;
        List<NormalDaBid> normalDaBids;
        List<Double> daForecastBids;

        @Override
        public String getAggregateId() {
            return unitId.toString();
        }
    }
}
