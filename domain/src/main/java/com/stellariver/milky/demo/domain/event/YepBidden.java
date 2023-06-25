package com.stellariver.milky.demo.domain.event;

import com.stellariver.milky.demo.basic.Direction;
import com.stellariver.milky.demo.basic.Transaction;
import com.stellariver.milky.demo.basic.UnitIdentify;
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
public class YepBidden extends Event {

    Direction direction;
    UnitIdentify unitIdentify;
    List<Transaction> transactions;

    @Override
    public String getAggregateId() {
        return unitIdentify.getUnitId();
    }

}
