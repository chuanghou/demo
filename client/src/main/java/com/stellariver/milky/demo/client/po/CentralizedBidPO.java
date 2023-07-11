package com.stellariver.milky.demo.client.po;

import com.stellariver.milky.demo.common.TxGroup;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CentralizedBidPO {

    @Valid
    TxGroup txGroup;

    @NotEmpty
    List<BidPO> bidPOs;

}
