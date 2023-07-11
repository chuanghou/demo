package com.stellariver.milky.demo.client.po;

import com.stellariver.milky.demo.common.Agent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompCreatePO {

    List<Agent> agents;
    String name;
}
