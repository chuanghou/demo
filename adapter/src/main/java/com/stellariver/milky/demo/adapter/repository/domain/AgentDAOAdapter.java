package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgentDAOAdapter implements DaoAdapter<User> {

    @Override
    public User toAggregate(@NonNull Object dataObject) {
        return Convertor.INST.to((UserDO) dataObject);
    }

    @Override
    public Object toDataObject(User user, DataObjectInfo dataObjectInfo) {
        return Convertor.INST.to(user);
    }

    @Override
    public DataObjectInfo dataObjectInfo(String aggregateId) {
        return DataObjectInfo.builder().clazz(UserDO.class).primaryId(aggregateId).build();
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        User to(UserDO userDO);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        UserDO to(User agent);


    }
}
