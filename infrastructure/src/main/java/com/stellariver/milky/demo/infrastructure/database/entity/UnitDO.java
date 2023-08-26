package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.stellariver.milky.demo.common.ForecastDaBid;
import com.stellariver.milky.demo.common.NormalDaBid;
import com.stellariver.milky.domain.support.base.BaseDataObject;
import com.stellariver.milky.infrastructure.base.database.AbstractMpDO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author houchuang
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "demo_unit", autoResultMap = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitDO extends AbstractMpDO implements BaseDataObject<Long> {

    @TableId(type = IdType.INPUT)
    Long unitId;
    Integer userId;
    Long compId;
    Integer roundId;

    Integer metaUnitId;
    String province;    // for query
    String unitType;    // for query

    @TableField("bid_pos")
    String bidPOs;

    String centralizedBids;
    String stageFourDirections;
    String balances;

    @TableField(exist = false)
    List<BidDO> bidDOs;

    List<NormalDaBid> normalDaBids;
    List<Double> forecastDaBids;

    @Override
    public Long getPrimaryId() {
        return unitId;
    }

}
