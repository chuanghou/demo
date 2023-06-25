package com.stellariver.milky.demo.domain.command;

import com.stellariver.milky.demo.basic.PodPos;
import com.stellariver.milky.demo.basic.PodType;
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

    UnitIdentify unitIdentify;
    PodPos podPos;
    PodType podType;
    String userId;
    Double capacity;

    @Override
    public String getAggregateId() {
        return unitIdentify.getUnitId();
    }

}
