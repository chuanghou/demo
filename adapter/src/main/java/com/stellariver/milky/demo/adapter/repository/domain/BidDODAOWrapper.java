package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.infrastructure.database.entity.BidDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.BidDOMapper;
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
public class BidDODAOWrapper implements DAOWrapper<BidDO, String> {

    final BidDOMapper bidDOMapper;

    @Override
    public int batchSave(@NonNull List<BidDO> bidDOS) {
        return bidDOS.stream().map(bidDOMapper::insert).reduce(0, Integer::sum);
    }

    @Override
    public int batchUpdate(@NonNull List<BidDO> bidDOs) {
        return bidDOs.stream().map(bidDOMapper::updateById).reduce(0, Integer::sum);
    }

    @Override
    public Map<String, BidDO> batchGetByPrimaryIds(@NonNull Set<String> ids) {
        List<BidDO> bidDOs = bidDOMapper.selectBatchIds(ids);
        return Collect.toMap(bidDOs, BidDO::getId);
    }

    @Override
    public BidDO merge(@NonNull BidDO priority, @NonNull BidDO original) {
        return Merger.INST.merge(priority, original);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Merger {

        Merger INST = Mappers.getMapper(Merger.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        BidDO merge(BidDO priority, @MappingTarget BidDO original);

    }
}
