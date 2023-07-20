package com.stellariver.milky.demo.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetaUnitIds {

    Long tgMetaUnitId;
    Long tlMetaUnitId;
    Long rgMetaUnitId;
    Long rlMetaUnitId;

}
