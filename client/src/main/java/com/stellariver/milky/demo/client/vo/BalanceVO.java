package com.stellariver.milky.demo.client.vo;

import com.stellariver.milky.demo.common.enums.Direction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceVO {
    Direction direction;
    Double balance;
}
