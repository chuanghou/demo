package com.stellariver.milky.demo.client.po;

import com.stellariver.milky.demo.common.enums.TxGroup;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RealtimeBidPO {

    @Valid
    TxGroup txGroup;

    @NotEmpty
    BidPO bidPO;

}
