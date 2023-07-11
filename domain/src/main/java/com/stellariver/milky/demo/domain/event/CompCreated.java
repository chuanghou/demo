package com.stellariver.milky.demo.domain.event;

import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.common.Agent;
import com.stellariver.milky.domain.support.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompCreated extends Event {

    String compId;
    String date;
    String name;
    Stage stage;
    List<Agent> agents;


    @Override
    public String getAggregateId() {
        return null;
    }
}
