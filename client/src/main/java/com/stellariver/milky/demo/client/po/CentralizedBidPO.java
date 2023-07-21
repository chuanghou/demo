package com.stellariver.milky.demo.client.po;

import com.stellariver.milky.demo.common.TxGroup;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CentralizedBidPO {

    @NotNull @Positive
    Long unitId;

    @NotEmpty
    List<BidPO> bids;

}
