package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.common.enums.Bid;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RealtimeBidContainer {

    Bid bid;

}
