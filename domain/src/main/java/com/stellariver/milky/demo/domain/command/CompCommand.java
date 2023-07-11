package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.common.Agent;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.domain.support.command.Command;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

public class CompCommand {
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Clear extends Command {

        String compId;

        Stage stage;

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
    public static class Create extends Command {

        String compId;
        String name;
        String date;
        List<Agent> agents;

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
    public static class Step extends Command {

        String compId;

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
    public static class RealtimeBid extends Command {

        String compId;
        Bid bid;

        @Override
        public String getAggregateId() {
            return compId;
        }

    }
}
