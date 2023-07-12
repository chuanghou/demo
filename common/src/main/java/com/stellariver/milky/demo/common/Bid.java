package com.stellariver.milky.demo.common;

import com.stellariver.milky.demo.common.enums.Direction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bid {

    String id;
    TxGroup txGroup;
    Direction direction;
    Double quantity;
    Double price;
    Date date;
    Stage stage;

}
