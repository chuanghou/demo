package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("transaction_unit_do")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionUnitDO extends AbstractMpDO implements BaseDataObject<String> {

    @TableId(type = IdType.INPUT)
    String id;
    Double capacity;
    Double bought;
    Double sold;

    @Override
    public String getPrimaryId() {
        return id;
    }

}
