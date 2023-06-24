package com.stellariver.milky.demo.adapter.controller.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RealtimeBidReq {

    UnitIdentifyReq unitIdentify;
    TransactionReq transaction;

}
