package com.stellariver.milky.demo.infrastructure.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stellariver.milky.demo.infrastructure.database.entity.BlockThermalUnitDO;
import com.stellariver.milky.demo.infrastructure.database.entity.LoadForecastDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoadForecastMapper extends BaseMapper<LoadForecastDO> {
}
