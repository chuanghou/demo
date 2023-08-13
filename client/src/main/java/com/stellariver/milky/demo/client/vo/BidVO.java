package com.stellariver.milky.demo.client.vo;

import com.stellariver.milky.demo.common.enums.BidStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidVO {

    Double quantity;
    String date;
    Double price;
    Double notDeal;
    Boolean cancelable;
    List<DealVO> dealVOs;

}
