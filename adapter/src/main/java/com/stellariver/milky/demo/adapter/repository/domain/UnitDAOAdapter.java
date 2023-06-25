package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.demo.basic.BasicConvertor;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
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
public class UnitDAOAdapter implements DaoAdapter<Unit> {

    @Override
    public Unit toAggregate(@NonNull Object dataObject) {
        return Convertor.INST.to((UnitDO) dataObject);
    }

    @Override
    public Object toDataObject(Unit item, DataObjectInfo dataObjectInfo) {
        return Convertor.INST.to(item);
    }

    @Override
    public DataObjectInfo dataObjectInfo(String aggregateId) {
        return DataObjectInfo.builder().clazz(UnitDO.class).primaryId(aggregateId).build();
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor extends BasicConvertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        @Mapping(source = "unitId", target = "unitIdentify")
        Unit to(UnitDO unitDO);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        @Mapping(source = "unitIdentify", target = "unitId")
        UnitDO to(Unit unit);

        @AfterMapping
        default void after(Unit unit, @MappingTarget UnitDO unitDO) {
            unitDO.setUserId(unit.getUnitIdentify().getUnitId());
            unitDO.setCompId(unit.getUnitIdentify().getCompId());
        }

    }
}
