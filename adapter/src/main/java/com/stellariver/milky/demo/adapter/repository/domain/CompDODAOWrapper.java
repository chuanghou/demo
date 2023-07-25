package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.common.tool.common.ConcurrentTool;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.BasicConvertor;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.domain.support.dependency.DAOWrapper;
import com.stellariver.milky.domain.support.util.ThreadLocalTransferableExecutor;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.Builder;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @author houchuang
 */
@CustomLog
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompDODAOWrapper implements DAOWrapper<Comp, Long> {

    final CompDOMapper compDOMapper;

    final Map<Long, Comp> compMap = new ConcurrentHashMap<>();

    final ThreadLocalTransferableExecutor executor;

    @Override
    @SneakyThrows
    public int batchSave(@NonNull List<Comp> comps) {
        comps.forEach(comp -> compMap.put(comp.getCompId(), comp));
        ConcurrentTool.batchCallFuture(comps, c -> compDOMapper.insert(Convertor.INST.to(c)), executor);
        return comps.size();
    }

    @Override
    public int batchUpdate(@NonNull List<Comp> comps) {
        comps.forEach(comp -> compMap.put(comp.getCompId(), comp));
        ConcurrentTool.batchCallFuture(comps, c -> compDOMapper.updateById(Convertor.INST.to(c)), executor);
        return comps.size();
    }

    @Override
    public Map<Long, Comp> batchGetByPrimaryIds(@NonNull Set<Long> ids) {
        Map<Long, Comp> comps = new HashMap<>();
        List<Long> omitIds = new ArrayList<>();
        ids.forEach(id -> {
            Comp comp = compMap.get(id);
            if (comp == null) {
                omitIds.add(id);
            } else {
                comps.put(id, comp);
            }
        });
        Map<Long, Comp> dbCompMap = ConcurrentTool.batchCall(omitIds, id -> Convertor.INST.to(compDOMapper.selectById(id)), executor);
        return Collect.mergeMightEx(comps, dbCompMap);
    }

    @Override
    public Comp merge(@NonNull Comp priority, @NonNull Comp original) {
        return priority;
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor extends BasicConvertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Comp to(CompDO compDO);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CompDO to(Comp comp);

    }

}
