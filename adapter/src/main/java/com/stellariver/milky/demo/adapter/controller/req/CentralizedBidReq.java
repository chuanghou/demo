package com.stellariver.milky.demo.adapter.controller.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CentralizedBidReq {

    @Valid
    UnitIdentifyReq unitIdentify;

    @Valid
    TransactionReq transaction;

}
