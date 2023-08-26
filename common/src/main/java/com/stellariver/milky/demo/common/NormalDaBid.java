package com.stellariver.milky.demo.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NormalDaBid {

    Double left;
    Double right;
    Double price;

}
