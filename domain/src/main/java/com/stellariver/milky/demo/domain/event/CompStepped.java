package com.stellariver.milky.demo.domain.event;

import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.domain.support.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompStepped extends Event {

    String compId;
    Stage lastStage;
    Stage nextStage;


    @Override
    public String getAggregateId() {
        return compId;
    }
}
