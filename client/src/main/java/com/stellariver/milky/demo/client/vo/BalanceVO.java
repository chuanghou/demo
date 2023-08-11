package com.stellariver.milky.demo.client.vo;

import com.stellariver.milky.demo.common.enums.Direction;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BalanceVO {

    Direction direction;
    Double balance;

}
