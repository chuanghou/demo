package com.stellariver.milky.demo.infrastructure.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stellariver.milky.demo.infrastructure.database.entity.GeneratorDO;
import com.stellariver.milky.demo.infrastructure.database.entity.GeneratorOutputStateDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author houchuang
 */
@Mapper
public interface GeneratorOutputStateMapper extends BaseMapper<GeneratorOutputStateDO> {
}
