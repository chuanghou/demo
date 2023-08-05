package com.stellariver.milky.demo.common;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.ErrorEnum;
import com.stellariver.milky.common.base.ErrorEnumsBase;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GridLimit {

    Double high;
    Double low;

    public void check(Double price) {
        if (price > high || price < low) {
            throw new BizEx(ErrorEnumsBase.PARAM_FORMAT_WRONG.message("价格不合适不满住要求"));
        }
    }
}
