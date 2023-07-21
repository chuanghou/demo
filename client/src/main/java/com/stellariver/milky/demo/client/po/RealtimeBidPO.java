package com.stellariver.milky.demo.client.po;

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
    Long unitId;

    @NotEmpty
    BidPO bidPO;

}
