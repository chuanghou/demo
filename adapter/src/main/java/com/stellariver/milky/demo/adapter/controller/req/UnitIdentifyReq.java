package com.stellariver.milky.demo.adapter.controller.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitIdentifyReq {
    String compId;
    String podId;
    String type;
    String date;
    String timeFrame;
}
