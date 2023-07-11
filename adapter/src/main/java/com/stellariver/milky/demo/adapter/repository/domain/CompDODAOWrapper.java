package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
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
import java.util.stream.Collectors;

/**
 * @author houchuang
 */
@CustomLog
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompDODAOWrapper implements DAOWrapper<CompDO, String> {

    final CompDOMapper compDOMapper;

    final Map<String, CompDO> compDOMap = new ConcurrentHashMap<>();

    @Override
    public int batchSave(@NonNull List<CompDO> compDOs) {
        CompletableFuture.runAsync(() -> {
            Integer reduce = compDOs.stream().map(compDOMapper::insert).reduce(0, Integer::sum);
            if (reduce != compDOs.size()) {
                log.error("ERROR" + compDOs);
            }
        });
        compDOs.forEach(compDO -> compDOMap.put(compDO.getCompId(), compDO));
        return compDOs.size();
    }

    @Override
    public int batchUpdate(@NonNull List<CompDO> compDOs) {
        CompletableFuture.runAsync(() -> {
            Integer reduce = compDOs.stream().map(compDOMapper::updateById).reduce(0, Integer::sum);
            if (reduce != compDOs.size()) {
                log.error("ERROR" + compDOs);
            }
        });
        compDOs.forEach(compDO -> compDOMap.put(compDO.getCompId(), compDO));
        return compDOs.size();
    }

    @Override
    public Map<String, CompDO> batchGetByPrimaryIds(@NonNull Set<String> ids) {
        List<CompDO> compDOs = ids.stream().map(compDOMap::get).collect(Collectors.toList());
        return Collect.toMap(compDOs, CompDO::getCompId);
    }

    @Override
    public CompDO merge(@NonNull CompDO priority, @NonNull CompDO original) {
        return Merger.INST.merge(priority, original);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Merger {

        Merger INST = Mappers.getMapper(Merger.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CompDO merge(CompDO priority, @MappingTarget CompDO original);

    }
}
