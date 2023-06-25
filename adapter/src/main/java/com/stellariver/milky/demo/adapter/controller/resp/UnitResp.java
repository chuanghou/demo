package com.stellariver.milky.demo.adapter.controller.resp;

import com.stellariver.milky.demo.basic.TimeFrame;
import com.stellariver.milky.demo.basic.UnitIdentify;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitResp {
    String compId;
    String comName;
    String podId;
    String podName;
    String timeFrame;
    Double capacity;
    Double bought;
    Double sold;

}
