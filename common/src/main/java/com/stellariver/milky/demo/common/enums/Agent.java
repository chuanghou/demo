package com.stellariver.milky.demo.common.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Agent {

    @NotBlank
    String userId;

    @NotEmpty
    List<String> metaUnitIds;

}
