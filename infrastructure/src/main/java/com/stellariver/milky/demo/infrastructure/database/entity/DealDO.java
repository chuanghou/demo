package com.stellariver.milky.demo.infrastructure.database.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DealDO {

    Long dealId;
    Long bidId;
    Long unitId;
    Double quantity;
    Double price;
    Date date;

}
