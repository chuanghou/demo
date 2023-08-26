package com.stellariver.milky.demo.infrastructure.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stellariver.milky.demo.infrastructure.database.entity.IntraDaGeneratorForecastBid;
import com.stellariver.milky.demo.infrastructure.database.entity.IntraDaLoadForecastBid;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author houchuang
 */
@Mapper
public interface IntraDaLoadForecastBidMapper extends BaseMapper<IntraDaLoadForecastBid> {
}
