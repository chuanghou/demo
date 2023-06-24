package com.stellariver.milky.demo.adapter.controller.req;

import com.stellariver.milky.demo.basic.Agent;
import com.stellariver.milky.demo.domain.command.CompBuild;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddCompReq {

    String date;
    String name;
    List<Agent> agents;

}
