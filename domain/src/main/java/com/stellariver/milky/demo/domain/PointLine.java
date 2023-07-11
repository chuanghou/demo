package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.TxGroup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@lombok.Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PointLine {

    String bidId;
    TxGroup txGroup;
    Direction direction;
    Double quantity;
    Double price;

    Double cumulateQuantity;
}
