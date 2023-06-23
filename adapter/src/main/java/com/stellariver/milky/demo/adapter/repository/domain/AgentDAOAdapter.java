package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.demo.domain.Agent;
import com.stellariver.milky.demo.infrastructure.database.entity.AgentDO;
import com.stellariver.milky.domain.support.dependency.DaoAdapter;
import com.stellariver.milky.domain.support.dependency.DataObjectInfo;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * @author houchuang
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AgentDAOAdapter implements DaoAdapter<Agent> {

    @Override
    public Agent toAggregate(@NonNull Object dataObject) {
        return Convertor.INST.to((AgentDO) dataObject);
    }

    @Override
    public Object toDataObject(Agent agent, DataObjectInfo dataObjectInfo) {
        return Convertor.INST.to(agent);
    }

    @Override
    public DataObjectInfo dataObjectInfo(String aggregateId) {
        return DataObjectInfo.builder().clazz(AgentDO.class).primaryId(aggregateId).build();
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Agent to(AgentDO agentDO);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        AgentDO to(Agent agent);


    }
}
