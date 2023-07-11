package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.common.TxGroup;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderIdentify {
    String id;
    TxGroup txGroup;
}
