package com.stellariver.milky.demo.domain.event;

import com.stellariver.milky.demo.basic.Transaction;
import com.stellariver.milky.demo.basic.UnitIdentify;
import com.stellariver.milky.domain.support.event.Event;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CentralizedBiddenEvent extends Event {

    UnitIdentify unitIdentify;
    Transaction transaction;

    @Override
    public String getAggregateId() {
        return unitIdentify.getUnitId();
    }

}
