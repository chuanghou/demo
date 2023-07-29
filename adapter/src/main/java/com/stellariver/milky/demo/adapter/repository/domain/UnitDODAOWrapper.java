package com.stellariver.milky.demo.adapter.repository.domain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.tool.common.ConcurrentTool;
import com.stellariver.milky.demo.infrastructure.database.entity.BidDO;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.BidDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.UnitDOMapper;
import com.stellariver.milky.domain.support.dependency.DAOWrapper;
import com.stellariver.milky.domain.support.util.ThreadLocalTransferableExecutor;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        List<BidDO> bidDOs = unitDOs.stream().map(UnitDO::getBidDOs).flatMap(Collection::stream).collect(Collectors.toList());
        ConcurrentTool.batchCall(bidDOs, this::saveOrUpdate, executor);
        return unitDOs.stream().map(unitDOMapper::insert).reduce(0, Integer::sum);
    }

    @Override
    @SneakyThrows
    public int batchUpdate(@NonNull List<UnitDO> unitDOs) {
        List<BidDO> bidDOs = unitDOs.stream().map(UnitDO::getBidDOs).flatMap(Collection::stream).collect(Collectors.toList());
        ConcurrentTool.batchCall(bidDOs, this::saveOrUpdate, executor);
        return unitDOs.stream().map(unitDOMapper::updateById).reduce(0, Integer::sum);

    }

    private int saveOrUpdate(BidDO bidDO) {
        LambdaQueryWrapper<BidDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BidDO::getBidId, bidDO.getBidId());
        if (bidDOMapper.exists(queryWrapper)) {
            return bidDOMapper.updateById(bidDO);
        } else {
            return bidDOMapper.insert(bidDO);
        }
    }

    @Override
    public Map<Long, UnitDO> batchGetByPrimaryIds(@NonNull Set<Long> ids) {
        return ConcurrentTool.batchCall(ids, id -> {
            UnitDO unitDO = unitDOMapper.selectById(id);
            LambdaQueryWrapper<BidDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BidDO::getUnitId, unitDO.getUnitId());
            List<BidDO> bidDOs = bidDOMapper.selectList(queryWrapper);
            unitDO.setBidDOs(bidDOs);
            return unitDO;
        }, executor);
    }

}
