package com.stellariver.milky.demo.domain.event;

import com.stellariver.milky.demo.domain.DealResult;
import com.stellariver.milky.domain.support.command.Command;
import com.stellariver.milky.domain.support.event.Event;
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
public class CompClearStagedOne extends Event {

    String compId;

    List<DealResult> dealResults;

    @Override
    public String getAggregateId() {
        return compId;
    }

}
