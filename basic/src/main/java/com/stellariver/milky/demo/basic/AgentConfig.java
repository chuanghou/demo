package com.stellariver.milky.demo.basic;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgentConfig {

    Integer roundId;
    Integer agentId;
    Pair<Integer, Integer> generatorIds;
    Pair<Integer, Integer> loadIds;

}
