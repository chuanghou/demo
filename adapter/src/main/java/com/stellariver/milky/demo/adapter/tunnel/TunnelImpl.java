package com.stellariver.milky.demo.adapter.tunnel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.domain.AbstractMetaUnit;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.GeneratorDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.LoadDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.UnitDOMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TunnelImpl implements Tunnel {

    final UnitDOMapper unitDOMapper;
    final GeneratorDOMapper generatorDOMapper;
    final LoadDOMapper loadDOMapper;

    @Override
    public List<Unit> getByCompId(Integer compId) {
        LambdaQueryWrapper<UnitDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UnitDO::getCompId, compId);
        List<UnitDO> unitDOs = unitDOMapper.selectList(lambdaQueryWrapper);
        return Collect.transfer(unitDOs, UnitDAOAdapter.Convertor.INST::to);
    }

    @Override
    public AbstractMetaUnit getByMetaUnitId(String metaUnitId) {
        return new AbstractMetaUnit();
    }

    @Override
    public long loadGeneratorNumber() {
        return generatorDOMapper.selectCount(null);
    }

    @Override
    public long loadLoadNumber() {
        return loadDOMapper.selectCount(null);
    }

}
