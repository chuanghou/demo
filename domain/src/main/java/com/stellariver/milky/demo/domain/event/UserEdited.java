package com.stellariver.milky.demo.domain.event;

import com.stellariver.milky.domain.support.event.Event;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEdited extends Event {

    String agentId;
    String oldName;
    String newName;
    String oldPassword;
    String newPassword;

    @Override
    public String getAggregateId() {
        return agentId;
    }

}
