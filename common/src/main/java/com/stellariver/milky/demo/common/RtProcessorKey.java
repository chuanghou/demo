package com.stellariver.milky.demo.common;

import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RtProcessorKey {

    Integer roundId;
    MarketType marketType;
    Province province;
    TimeFrame timeFrame;

}
