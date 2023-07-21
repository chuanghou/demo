package com.stellariver.milky.demo.adapter.bo;

import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.TxGroup;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RealtimeBid {

    TxGroup txGroup;

    Bid bid;

}
