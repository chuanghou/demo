package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UnitDOMapper;
import com.stellariver.milky.domain.support.dependency.DAOWrapper;
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



    @Override
    public int batchSave(@NonNull List<UnitDO> unitDOs) {
        return unitDOs.stream().map(unitDOMapper::insert).reduce(0, Integer::sum);
    }

    @Override
    public int batchUpdate(@NonNull List<UnitDO> unitDOs) {
        return unitDOs.stream().map(unitDOMapper::updateById).reduce(0, Integer::sum);

    }

    @Override
    public Map<Long, UnitDO> batchGetByPrimaryIds(@NonNull Set<Long> ids) {
        return Collect.toMap(unitDOMapper.selectBatchIds(ids), UnitDO::getUnitId);
    }

    @Override
    public UnitDO merge(@NonNull UnitDO priority, @NonNull UnitDO original) {
        return Merger.INST.merge(priority, original);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Merger {

        Merger INST = Mappers.getMapper(Merger.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        UnitDO merge(UnitDO priority, @MappingTarget UnitDO original);

    }
}
