package com.stellariver.milky.demo.adapter.controller.resp;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitResp {
    String unitId;
    String compId;
    String comName;
    String podId;
    String podName;
    String timeFrame;
    Double capacity;
    Double bought;
    Double sold;

}
