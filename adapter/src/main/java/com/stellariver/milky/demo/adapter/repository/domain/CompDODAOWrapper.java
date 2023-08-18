package com.stellariver.milky.demo.adapter.repository.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.stellariver.milky.common.tool.common.ConcurrentTool;
import com.stellariver.milky.common.tool.executor.ThreadLocalTransferableExecutor;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.basic.BasicConvertor;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.domain.support.dependency.DAOWrapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Builder;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    public List<Comp> memoryComps() {
        return new ArrayList<>(compMap.values());
    }

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
         return Collect.toMapMightEx(compMap.values(), Comp::getCompId);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor extends BasicConvertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        @Mapping(source = "durations", target = "durations", ignore = true)
        Comp to(CompDO compDO);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        @Mapping(source = "durations", target = "durations", ignore = true)
        CompDO to(Comp comp);


        @AfterMapping
        default void after(Comp comp, @MappingTarget CompDO compDO) {
            Map<MarketType, Map<Status.MarketStatus, Duration>> durations = comp.getDurations();
            HashMap<MarketType, Map<Status.MarketStatus, Long>> rawDurations = new HashMap<>();
            durations.forEach((mT, map) -> {
                Map<Status.MarketStatus, Long> subMap = new HashMap<>();
                map.forEach((k, v) -> subMap.put(k, v.get(ChronoUnit.SECONDS)));
                rawDurations.put(mT, subMap);
            });
            compDO.setDurations(Json.toJson(rawDurations));
        }

        @AfterMapping
        default void after(CompDO compDO, @MappingTarget Comp comp) {
            Map<MarketType, Map<Status.MarketStatus, Long>> rawDurations = Json.parse(compDO.getDurations(),
                    new TypeReference<Map<MarketType, Map<Status.MarketStatus, Long>>>() {
                    });
            HashMap<MarketType, Map<Status.MarketStatus, Duration>> durations = new HashMap<>();
            rawDurations.forEach((mT, map) -> {
                Map<Status.MarketStatus, Duration> subMap = new HashMap<>();
                map.forEach((k, v) -> subMap.put(k, Duration.of(v, ChronoUnit.SECONDS)));
                durations.put(mT, subMap);
            });
            comp.setDurations(durations);
        }

    }

}
