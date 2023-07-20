package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.domain.MetaUnitIds;
import com.stellariver.milky.domain.support.command.Command;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

public class AgentCommand {

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create extends Command {

        Long agentId;
        Long compId;
        Long roundId;
        Long userId;
        List<Long> metaUnitIds;

        @Override
        public String getAggregateId() {
            return agentId.toString();
        }

    }
}
