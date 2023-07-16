package com.stellariver.milky.demo.domain.event;

import com.stellariver.milky.demo.common.Agent;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.domain.DealResult;
import com.stellariver.milky.domain.support.event.Event;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;
import java.util.List;

public class CompEvent {

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Cleared extends Event {

        Integer compId;

        MarketType marketType;

        List<DealResult> dealResults;

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

        String compId;
        String date;
        String name;
        MarketType marketType;
        List<Agent> agents;


        @Override
        public String getAggregateId() {
            return null;
        }
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Stepped extends Event {

        String compId;

        @Nullable
        Integer lastRoundId;
        @Nullable
        MarketType lastMarketType;
        @Nullable
        Boolean lastMarketStatus;

        Integer nextRoundId;
        MarketType nextMarketType;
        Boolean nextMarketStatus;

        @Override
        public String getAggregateId() {
            return compId;
        }
    }

}
