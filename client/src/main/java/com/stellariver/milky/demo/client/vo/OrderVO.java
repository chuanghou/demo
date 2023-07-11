package com.stellariver.milky.demo.client.vo;

import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Deal;
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
public class OrderVO {

    String id;
    TxGroupVO txGroupVO;
    Bid bid;
    List<Deal> deals;
    Double remainder;

}
