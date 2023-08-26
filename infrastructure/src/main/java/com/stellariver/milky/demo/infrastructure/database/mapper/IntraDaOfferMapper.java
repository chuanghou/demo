package com.stellariver.milky.demo.infrastructure.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stellariver.milky.demo.infrastructure.database.entity.InterDaUO;
import com.stellariver.milky.demo.infrastructure.database.entity.IntraDaUO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author houchuang
 */
@Mapper
public interface IntraDaOfferMapper extends BaseMapper<IntraDaUO> {
}
