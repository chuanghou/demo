package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.enums.CancelBid;
import com.stellariver.milky.demo.common.enums.NewBid;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RtBidContainer {

    NewBid newBid;
    CancelBid cancelBid;
    Boolean close;

}
