package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.common.Agent;
import com.stellariver.milky.domain.support.command.Command;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompCreate extends Command {

    String compId;
    String name;
    String date;
    List<Agent> agents;

    @Override
    public String getAggregateId() {
        return compId;
    }

}
