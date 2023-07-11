package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.domain.support.command.Command;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompClearStageOne extends Command {

    String compId;

    @Override
    public String getAggregateId() {
        return compId;
    }

}
