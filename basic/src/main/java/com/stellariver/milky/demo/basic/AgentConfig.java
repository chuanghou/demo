package com.stellariver.milky.demo.basic;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgentConfig {

    Integer roundId;
    Integer agentId;
    Integer generatorId0;
    Integer generatorId1;
    Integer loadId0;
    Integer loadId1;

}
