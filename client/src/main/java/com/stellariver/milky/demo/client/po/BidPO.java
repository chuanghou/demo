package com.stellariver.milky.demo.client.po;

import com.stellariver.milky.common.base.OfEnum;
import com.stellariver.milky.demo.common.enums.Direction;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BidPO {

    @NotNull
    @OfEnum(enumType = Direction.class)
    String direction;

    @NotNull
    @Positive
    Double quantity;

    @NotNull
    @Positive
    Double price;

}
