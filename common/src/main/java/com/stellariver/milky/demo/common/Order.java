package com.stellariver.milky.demo.common;

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

    TxGroup txGroup;
    Bid bid;
    @Builder.Default
    Double cancelled = 0D;
    @Builder.Default
    List<Deal> deals = new ArrayList<>();

    public String getId() {
        return bid.getId();
    }

}
