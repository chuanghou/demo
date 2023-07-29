package com.stellariver.milky.demo.client.po;

import com.stellariver.milky.demo.common.Agent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompCreatePO {

    @NotNull @Min(1) @Max(15)
    Integer agentNumber;

    @Size(min = 7, max = 7)
    List<Long> durations; // seconds

}
