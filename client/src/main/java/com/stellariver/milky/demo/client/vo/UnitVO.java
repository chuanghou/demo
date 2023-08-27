package com.stellariver.milky.demo.client.vo;

import com.stellariver.milky.demo.common.enums.GeneratorType;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.common.enums.UnitType;
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
public class UnitVO {

    Long unitId;
    String name;
    UnitType unitType;
    GeneratorType generatorType;
    Province province;
    TimeFrame timeFrame;
    CapacityVO capacityVO;
    List<BidVO> bidVOs;

}
