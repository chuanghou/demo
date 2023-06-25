package com.stellariver.milky.demo.adapter.controller.resp;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PodResp {
    String podId;
    String podName;
    String podType;
    Double peakCapacity;
    Double flatCapacity;
    Double valleyCapacity;
}
