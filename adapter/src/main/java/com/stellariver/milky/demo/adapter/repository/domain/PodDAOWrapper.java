package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.infrastructure.database.entity.PodDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.PodDOMapper;
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
public class PodDAOWrapper implements DAOWrapper<PodDO, String> {

    final PodDOMapper PodDOMapper;

    @Override
    public int batchSave(@NonNull List<PodDO> podDOS) {
        return podDOS.stream().map(PodDOMapper::insert).reduce(0, Integer::sum);
    }

    @Override
    public int batchUpdate(@NonNull List<PodDO> itemDOs) {
        return itemDOs.stream().map(PodDOMapper::updateById).reduce(0, Integer::sum);
    }

    @Override
    public Map<String, PodDO> batchGetByPrimaryIds(@NonNull Set<String> ids) {
        List<PodDO> podDOS = PodDOMapper.selectBatchIds(ids);
        return Collect.toMap(podDOS, PodDO::getId);
    }

    @Override
    public PodDO merge(@NonNull PodDO priority, @NonNull PodDO original) {
        return Merger.INST.merge(priority, original);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Merger {

        Merger INST = Mappers.getMapper(Merger.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        PodDO merge(PodDO priority, @MappingTarget PodDO original);

    }
}
