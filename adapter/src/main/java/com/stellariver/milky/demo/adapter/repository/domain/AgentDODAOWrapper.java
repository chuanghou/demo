package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.infrastructure.database.entity.AgentDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.AgentDOMapper;
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
public class AgentDODAOWrapper implements DAOWrapper<AgentDO, String> {

    final AgentDOMapper AgentDOMapper;

    @Override
    public int batchSave(@NonNull List<AgentDO> AgentDOs) {
        return AgentDOs.stream().map(AgentDOMapper::insert).reduce(0, Integer::sum);
    }

    @Override
    public int batchUpdate(@NonNull List<AgentDO> itemDOs) {
        return itemDOs.stream().map(AgentDOMapper::updateById).reduce(0, Integer::sum);
    }

    @Override
    public Map<String, AgentDO> batchGetByPrimaryIds(@NonNull Set<String> ids) {
        List<AgentDO> AgentDOs = AgentDOMapper.selectBatchIds(ids);
        return Collect.toMap(AgentDOs, AgentDO::getId);
    }

    @Override
    public AgentDO merge(@NonNull AgentDO priority, @NonNull AgentDO original) {
        return Merger.INST.merge(priority, original);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Merger {

        Merger INST = Mappers.getMapper(Merger.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        AgentDO merge(AgentDO priority, @MappingTarget AgentDO original);

    }
}
