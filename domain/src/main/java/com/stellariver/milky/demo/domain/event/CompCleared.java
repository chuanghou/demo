package com.stellariver.milky.demo.domain.event;

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
public class CompCleared extends Event {

    String compId;

    @Override
    public String getAggregateId() {
        return compId;
    }

}
