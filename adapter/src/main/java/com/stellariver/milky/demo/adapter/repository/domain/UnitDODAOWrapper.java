package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UnitDOMapper;
import com.stellariver.milky.domain.support.dependency.DAOWrapper;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author houchuang
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitDODAOWrapper implements DAOWrapper<UnitDO, String> {

    final UnitDOMapper unitDOMapper;

    @Override
    public int batchSave(@NonNull List<UnitDO> unitDOS) {
        return unitDOS.stream().map(unitDOMapper::insert).reduce(0, Integer::sum);
    }

    @Override
    public int batchUpdate(@NonNull List<UnitDO> itemDOs) {
        return itemDOs.stream().map(unitDOMapper::updateById).reduce(0, Integer::sum);
    }

    @Override
    public Map<String, UnitDO> batchGetByPrimaryIds(@NonNull Set<String> ids) {
        List<UnitDO> unitDOS = unitDOMapper.selectBatchIds(ids);
        return Collect.toMap(unitDOS, UnitDO::getId);
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
