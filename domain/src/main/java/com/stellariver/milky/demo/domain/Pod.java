package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.PodPos;
import com.stellariver.milky.demo.basic.PodType;
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

    String podId;
    String name;
    PodType podType;
    PodPos podPos;
    Double peakCapacity;
    Double flatCapacity;
    Double valleyCapacity;

    @Override
    public String getAggregateId() {
        return podId;
    }
}
