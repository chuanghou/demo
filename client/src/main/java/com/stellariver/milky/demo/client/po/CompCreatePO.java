package com.stellariver.milky.demo.client.po;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompCreatePO {

    @NotNull @Min(1) @Max(15)
    Integer agentNumber;

    @NotNull
    @Size(min = 7, max = 7)
    Map<String, Map<String, Integer>> durations; // seconds

}
