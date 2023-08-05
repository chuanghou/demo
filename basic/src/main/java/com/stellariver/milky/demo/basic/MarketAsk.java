package com.stellariver.milky.demo.basic;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarketAsk {

    Double price;
    Double quantity;

}
