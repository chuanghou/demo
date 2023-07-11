package com.stellariver.milky.demo.client.po;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepCompPO {

    String compId;
    Long length; // unit second

}
