package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.basic.Direction;
import com.stellariver.milky.demo.basic.Transaction;
import com.stellariver.milky.demo.basic.UnitIdentify;
import com.stellariver.milky.domain.support.command.Command;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Yearly Exterior Provincial
 * @author houchuang
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class YepBid extends Command {

    Direction direction;
    UnitIdentify unitIdentify;
    List<Transaction> transactions;

    @Override
    public String getAggregateId() {
        return unitIdentify.getUnitId();
    }

}
