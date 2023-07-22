package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.common.TxGroup;
import com.stellariver.milky.demo.common.enums.Direction;
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

    Long bidId;
    Long unitId;
    Direction direction;
    Double quantity;
    Double price;

    Double leftX;
    Double rightX;
    Double width;
    Double y;
}
