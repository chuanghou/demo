package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.infrastructure.database.entity.TransactionUnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.TransactionUnitDOMapper;
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
public class TransactionUnitDODAOWrapper implements DAOWrapper<TransactionUnitDO, String> {

    final TransactionUnitDOMapper transactionUnitDOMapper;

    @Override
    public int batchSave(@NonNull List<TransactionUnitDO> transactionUnitDOs) {
        return transactionUnitDOs.stream().map(transactionUnitDOMapper::insert).reduce(0, Integer::sum);
    }

    @Override
    public int batchUpdate(@NonNull List<TransactionUnitDO> itemDOs) {
        return itemDOs.stream().map(transactionUnitDOMapper::updateById).reduce(0, Integer::sum);
    }

    @Override
    public Map<String, TransactionUnitDO> batchGetByPrimaryIds(@NonNull Set<String> ids) {
        List<TransactionUnitDO> transactionUnitDOs = transactionUnitDOMapper.selectBatchIds(ids);
        return Collect.toMap(transactionUnitDOs, TransactionUnitDO::getId);
    }

    @Override
    public TransactionUnitDO merge(@NonNull TransactionUnitDO priority, @NonNull TransactionUnitDO original) {
        return Merger.INST.merge(priority, original);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Merger {

        Merger INST = Mappers.getMapper(Merger.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        TransactionUnitDO merge(TransactionUnitDO priority, @MappingTarget TransactionUnitDO original);

    }
}
