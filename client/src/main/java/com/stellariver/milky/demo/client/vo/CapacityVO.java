package com.stellariver.milky.demo.client.vo;

import com.stellariver.milky.demo.common.enums.Direction;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CapacityVO {

    List<BalanceVO> balanceVOs;
    Double capacity;
    Double dealed;
    Double onMatching;
}
