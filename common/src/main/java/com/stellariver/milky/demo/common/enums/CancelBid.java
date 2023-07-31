package com.stellariver.milky.demo.common.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancelBid {

    Long bidId;
    Direction direction;

}
