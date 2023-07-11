package com.stellariver.milky.demo.client.vo;

import com.stellariver.milky.demo.common.enums.Bid;
import com.stellariver.milky.demo.common.enums.Deal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BidVO {

    String id;
    TxGroupVO txGroupVO;
    Bid bid;
    List<Deal> deals;
    Double remainder;

}
