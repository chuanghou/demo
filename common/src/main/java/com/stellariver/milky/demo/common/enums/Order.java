package com.stellariver.milky.demo.common.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    String id;
    TxGroup txGroup;
    Bid bid;
    @Builder.Default
    Double cancelled = 0D;
    @Builder.Default
    List<Deal> deals = new ArrayList<>();

}
