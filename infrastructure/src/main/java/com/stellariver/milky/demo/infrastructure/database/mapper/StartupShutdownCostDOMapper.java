package com.stellariver.milky.demo.infrastructure.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stellariver.milky.demo.infrastructure.database.entity.LoadDO;
import com.stellariver.milky.demo.infrastructure.database.entity.StartupShutdownCostDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author houchuang
 */
@Mapper
public interface StartupShutdownCostDOMapper extends BaseMapper<StartupShutdownCostDO> {
}
