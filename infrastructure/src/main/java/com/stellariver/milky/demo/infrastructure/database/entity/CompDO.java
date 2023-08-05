package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.domain.support.base.BaseDataObject;
import com.stellariver.milky.infrastructure.base.database.AbstractMpDO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("demo_comp")
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompDO extends AbstractMpDO implements BaseDataObject<Long> {

    @TableId(type = IdType.INPUT)
    Long compId;

    Integer userTotal;
    Integer roundTotal;
    Integer roundId;
    String compStatus;
    String marketType;
    String marketStatus;
    String priceLimit;
    String transLimit;
    @TableField(typeHandler = JacksonTypeHandler.class)
    Map<MarketType, Map<Status.MarketStatus, Duration>> durations;
    String replenishes;
    String roundCentralizedDeals;
    @Override
    public Long getPrimaryId() {
        return compId;
    }


}
