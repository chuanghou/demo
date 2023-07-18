package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.domain.support.command.Command;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

public class CompCommand {

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Reset extends Command {

        String compId;
        Integer agentNumber;

        @Override
        public String getAggregateId() {
            return compId;
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Clear extends Command {

        String compId;

        MarketType marketType;

        @Override
        public String getAggregateId() {
            return compId;
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Start extends Command {

        Integer compId;

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

        Integer compId;

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

        Integer compId;

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

        String compId;
        Bid bid;

        @Override
        public String getAggregateId() {
            return compId;
        }

    }
}
