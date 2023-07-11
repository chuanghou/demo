package com.stellariver.milky.demo.client.po;

import com.stellariver.milky.demo.common.TxGroup;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancelBidPO {

    @Valid
    TxGroup txGroup;

    @NotBlank
    String bidId;

}
