package com.stellariver.milky.demo.domain;

import com.stellariver.milky.domain.support.base.AggregateRoot;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pod extends AggregateRoot {

    String id;

    @Override
    public String getAggregateId() {
        return id;
    }
}
