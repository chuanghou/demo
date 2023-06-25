package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.basic.Stage;
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
public class CompClear extends Command {

    String compId;
    Stage stage;

    @Override
    public String getAggregateId() {
        return compId;
    }

}
