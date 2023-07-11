package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.common.enums.Deal;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DealResult {
    String bidId;
    Deal deal;
}
