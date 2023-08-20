package com.stellariver.milky.demo.adapter.po;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionPO {

    Long unitId;
    Double left;
    Double right;
    Double ratio;

}
