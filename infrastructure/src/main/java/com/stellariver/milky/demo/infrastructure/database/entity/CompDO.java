package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.stellariver.milky.domain.support.base.BaseDataObject;
import com.stellariver.milky.infrastructure.base.database.AbstractMpDO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("market_setting")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompDO extends AbstractMpDO implements BaseDataObject<Integer> {

    @TableId(type = IdType.INPUT)
    Integer marketSettingId;
    Integer roundNum;
    Integer roundId;
    Integer marketType;
    Integer intraprovincialAnnualBidDuration;
    Integer intraprovincialMonthlyBidDuration;
    Integer interprovincialAnnualBidDuration;
    Integer interprovincialSpotBidDuration;
    Integer intraprovincialSpotBidDuration;
    String agents;
    Long marketCloseTime;
    Boolean marketStatus;
    @Override
    public Integer getPrimaryId() {
        return marketSettingId;
    }


}
