package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.basic.UnitIdentify;
import com.stellariver.milky.domain.support.command.Command;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * @author houchuang
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitBuild extends Command {

    String bidId;
    UnitIdentify unitIdentify;
    Double capacity;

    @Override
    public String getAggregateId() {
        return unitIdentify.getUnitId();
    }

}
