package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.basic.Position;
import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.TxGroup;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.domain.support.command.Command;
import com.stellariver.milky.domain.support.event.Event;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

public class UnitCommand {

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DealReport extends Command {

        String bidId;
        TxGroup txGroup;
        Deal deal;

        @Override
        public String getAggregateId() {
            return txGroup.getUnitId();
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
    public static class RealtimeBid extends Command {

        TxGroup txGroup;
        Bid bid;

        @Override
        public String getAggregateId() {
            return txGroup.getUnitId();
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

        List<Bid> bids;

        @Override
        public String getAggregateId() {
            return getTxGroup().getUnitId();
        }

        public TxGroup getTxGroup() {
            return bids.get(0).getTxGroup();
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

        String unitId;

        @Override
        public String getAggregateId() {
            return unitId;
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
        Long compId;
        Long roundId;
        Long agentId;
        Long metaUnitId;

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

        String unitId;

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
    public static class Cancel extends Command {

        String orderId;
        TxGroup txGroup;

        @Override
        public String getAggregateId() {
            return txGroup.getUnitId();
        }
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CancelReport extends Command {

        String orderId;
        TxGroup txGroup;
        Double quantity;

        @Override
        public String getAggregateId() {
            return txGroup.getUnitId();
        }
    }
}
