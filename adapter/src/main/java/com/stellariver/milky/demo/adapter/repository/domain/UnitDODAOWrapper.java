package com.stellariver.milky.demo.adapter.repository.domain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.tool.common.ConcurrentTool;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.infrastructure.database.entity.BidDO;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.BidDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.UnitDOMapper;
import com.stellariver.milky.domain.support.dependency.DAOWrapper;
import com.stellariver.milky.domain.support.util.ThreadLocalTransferableExecutor;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author houchuang
 */
@CustomLog
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitDODAOWrapper implements DAOWrapper<UnitDO, Long> {

    final UnitDOMapper unitDOMapper;
    final BidDOMapper bidDOMapper;
    final ThreadLocalTransferableExecutor executor;

    @Override
    public int batchSave(@NonNull List<UnitDO> unitDOs) {
        List<BidDO> bidDOs = unitDOs.stream().flatMap(unitDO -> unitDO.getBidDOs().stream()).collect(Collectors.toList());
        ConcurrentTool.batchCall(bidDOs, bidDOMapper::insert, executor);
        return unitDOs.stream().map(unitDOMapper::insert).reduce(0, Integer::sum);
    }

    @Override
    public int batchUpdate(@NonNull List<UnitDO> unitDOs) {
        List<BidDO> bidDOs = unitDOs.stream().flatMap(unitDO -> unitDO.getBidDOs().stream()).collect(Collectors.toList());
        ConcurrentTool.batchCall(bidDOs, this::saveOrUpdate, executor);
        return unitDOs.stream().map(unitDOMapper::updateById).reduce(0, Integer::sum);

    }

    @Override
    public Map<Long, UnitDO> batchGetByPrimaryIds(@NonNull Set<Long> ids) {
        Map<Long, List<BidDO>> bidDOMap = ConcurrentTool.batchCall(ids, id -> {
            LambdaQueryWrapper<BidDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BidDO::getUnitId, id);
            return bidDOMapper.selectList(queryWrapper);
        }, executor);
        List<UnitDO> unitDOs = unitDOMapper.selectBatchIds(ids);
        unitDOs.forEach(unitDO -> unitDO.setBidDOs(bidDOMap.get(unitDO.getUnitId())));
        return Collect.toMap(unitDOs, UnitDO::getUnitId);
    }

    private int saveOrUpdate(BidDO bidDO) {
        if (bidDOMapper.selectById(bidDO.getBidId()) == null) {
            return bidDOMapper.insert(bidDO);
        } else {
            return bidDOMapper.updateById(bidDO);
        }
    }
}
