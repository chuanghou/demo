package com.stellariver.milky.demo.infrastructure.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stellariver.milky.demo.infrastructure.database.entity.BidDO;
import com.stellariver.milky.demo.infrastructure.database.entity.ThermalUnitCostDO;
import com.stellariver.milky.demo.infrastructure.database.entity.ThermalUnitOperatingCost;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author houchuang
 */
@Mapper
public interface ThermalUnitOperatingCostMapper extends BaseMapper<ThermalUnitOperatingCost> {
}
