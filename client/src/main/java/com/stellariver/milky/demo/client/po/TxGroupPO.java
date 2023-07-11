package com.stellariver.milky.demo.client.po;

import com.stellariver.milky.common.base.OfEnum;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TxGroupPO {

    @NotBlank
    String unitId;

    @NotNull
    @OfEnum(enumType = TimeFrame.class)
    String timeFrame;

}
