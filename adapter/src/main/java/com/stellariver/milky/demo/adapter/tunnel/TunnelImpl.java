package com.stellariver.milky.demo.adapter.tunnel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.domain.AbstractMetaUnit;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.MetaUnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.MetaUnitDOMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TunnelImpl implements Tunnel {

    final MetaUnitDOMapper metaUnitDOMapper;


    @Override
    public List<Unit> getByCompId(Long compId) {
        return null;
    }

    @Override
    public List<Unit> listUnitsByCompId(Long compId) {
        return null;
    }

    @Override
    public AbstractMetaUnit getByMetaUnitId(String metaUnitId) {
        return null;
    }

    @Override
    public long loadGeneratorNumber() {
        return 0;
    }

    @Override
    public long loadLoadNumber() {
        return 0;
    }

    @Override
    public long loadUnitNumber() {
        return 0;
    }

    @Override
    public Map<Integer, AbstractMetaUnit> getMetaUnitsByIds(Set<Integer> metaUnitIds) {
        LambdaQueryWrapper<MetaUnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MetaUnitDO::getMetaUnitId, metaUnitIds);
        Map<Integer, AbstractMetaUnit> metaUnitMap = new HashMap<>();
        metaUnitDOMapper.selectList(queryWrapper).forEach(metaUnitDO -> {
            AbstractMetaUnit abstractMetaUnit = UnitDAOAdapter.Convertor.INST.to(metaUnitDO);
            metaUnitMap.put(metaUnitDO.getMetaUnitId(), abstractMetaUnit);
        });
        return metaUnitMap;
    }

    @Override
    public Set<Integer> getMetaUnitIdBySourceIds(Set<Integer> metaUnitSourceIds) {
        LambdaQueryWrapper<MetaUnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MetaUnitDO::getSourceId, metaUnitSourceIds);
        return metaUnitDOMapper.selectList(queryWrapper).stream().map(MetaUnitDO::getMetaUnitId).collect(Collectors.toSet());
    }

    @Override
    public Comp currentComp() {
        return null;
    }
}
