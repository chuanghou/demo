package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.demo.domain.Pod;
import com.stellariver.milky.demo.infrastructure.database.entity.PodDO;
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
public class PodDAOAdapter implements DaoAdapter<Pod> {

    @Override
    public Pod toAggregate(@NonNull Object dataObject) {
        return Convertor.INST.to((PodDO) dataObject);
    }

    @Override
    public Object toDataObject(Pod pod, DataObjectInfo dataObjectInfo) {
        return Convertor.INST.to(pod);
    }

    @Override
    public DataObjectInfo dataObjectInfo(String aggregateId) {
        return DataObjectInfo.builder().clazz(PodDO.class).primaryId(aggregateId).build();
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Pod to(PodDO podDO);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        PodDO to(Pod pod);


    }
}
