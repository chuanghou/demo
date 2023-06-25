package com.stellariver.milky.demo.adapter.controller.resp;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgentResp {

    String userId;
    String userName;
    List<PodResp> pods;

}
