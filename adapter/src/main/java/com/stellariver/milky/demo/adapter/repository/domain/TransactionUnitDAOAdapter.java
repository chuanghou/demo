package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.demo.basic.UnitIdentify;
import com.stellariver.milky.demo.domain.TransactionUnit;
import com.stellariver.milky.demo.infrastructure.database.entity.TransactionUnitDO;
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
public class TransactionUnitDAOAdapter implements DaoAdapter<TransactionUnit> {

    @Override
    public TransactionUnit toAggregate(@NonNull Object dataObject) {
        return Convertor.INST.to((TransactionUnitDO) dataObject);
    }

    @Override
    public Object toDataObject(TransactionUnit item, DataObjectInfo dataObjectInfo) {
        return Convertor.INST.to(item);
    }

    @Override
    public DataObjectInfo dataObjectInfo(String aggregateId) {
        return DataObjectInfo.builder().clazz(TransactionUnitDO.class).primaryId(aggregateId).build();
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        TransactionUnit to(TransactionUnitDO transactionUnitDO);

        @AfterMapping
        default void after(TransactionUnitDO transactionUnitDO, @MappingTarget TransactionUnit transactionUnit) {
            transactionUnit.setUnitIdentify(UnitIdentify.resolve(transactionUnitDO.getId()));
        }

        @BeanMapping(builder = @Builder(disableBuilder = true))
        TransactionUnitDO to(TransactionUnit item);

        @AfterMapping
        default void after(TransactionUnit transactionUnit, @MappingTarget TransactionUnitDO transactionUnitDO) {
            UnitIdentify unitIdentify = UnitIdentify.resolve(transactionUnitDO.getId());
            transactionUnit.setUnitIdentify(unitIdentify);

        }

    }
}
