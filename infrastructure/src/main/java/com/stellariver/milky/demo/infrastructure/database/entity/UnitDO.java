package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.stellariver.milky.domain.support.base.BaseDataObject;
import com.stellariver.milky.infrastructure.base.database.AbstractMpDO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * @author houchuang
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("unit_do")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitDO extends AbstractMpDO implements BaseDataObject<String> {

    @TableId(type = IdType.INPUT)
    String unitId;
    String compId;
    String position;
    String unitType;
    String userId;
    String balanceQuantities;
    String centralizedBids;
    String orders;
    String stageFourDirection;

    @TableField(exist = false)
    Object unit;

    @Override
    public String getPrimaryId() {
        return unitId;
    }

}
