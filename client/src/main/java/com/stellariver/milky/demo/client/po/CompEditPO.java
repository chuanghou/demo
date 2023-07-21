package com.stellariver.milky.demo.client.po;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompEditPO {

    Long compId;
    List<Map<String, Long>> durations;

}
