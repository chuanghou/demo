package com.stellariver.milky.demo.adapter.controller.resp;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompResp {

    String compId;
    String date;
    String name;
    String stage;
    List<AgentResp> agents;

}
