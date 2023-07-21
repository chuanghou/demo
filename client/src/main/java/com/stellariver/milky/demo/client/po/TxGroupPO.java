package com.stellariver.milky.demo.client.po;

import com.stellariver.milky.common.base.OfEnum;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TxGroupPO {

    @NotNull @Positive
    Long unitId;

    @NotNull @OfEnum(enumType = TimeFrame.class)
    String timeFrame;

}
