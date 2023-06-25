package com.stellariver.milky.demo.adapter.controller.req;

import com.stellariver.milky.common.base.OfEnum;
import com.stellariver.milky.demo.basic.PodType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddPodReq {

    @NotBlank
    String name;
    @OfEnum(enumType = PodType.class)
    String podType;

    @Positive
    Double peakCapacity;
    @Positive
    Double flatCapacity;
    @Positive
    Double valleyCapacity;

}
