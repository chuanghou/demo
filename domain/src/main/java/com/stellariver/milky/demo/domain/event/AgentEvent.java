package com.stellariver.milky.demo.domain.event;

import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.domain.Agent;
import com.stellariver.milky.domain.support.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;

public class AgentEvent {


    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Created extends Event {

        Long agentId;
        Agent agent;

        @Override
        public String getAggregateId() {
            return agentId.toString();
        }
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Started extends Event {

        Long compId;
        Status.CompStatus lastCompStatus;
        Status.CompStatus nextCompStatus;
        Long roundId;
        MarketType marketType;
        Status.MarketStatus marketStatus;

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

        Integer compId;

        @Nullable
        Integer lastRoundId;
        @Nullable
        MarketType lastMarketType;
        @Nullable
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
    public static class Closed extends Event {

        Integer compId;
        Integer roundId;
        MarketType marketType;

        @Override
        public String getAggregateId() {
            return compId.toString();
        }
    }

}
