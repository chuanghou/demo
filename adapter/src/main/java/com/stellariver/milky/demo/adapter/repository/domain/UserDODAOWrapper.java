package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UserDOMapper;
import com.stellariver.milky.domain.support.ErrorEnums;
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

/**
 * @author houchuang
 */
@CustomLog
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDODAOWrapper implements DAOWrapper<UserDO, Integer> {

    final UserDOMapper userDOMapper;

    @Override
    public int batchSave(@NonNull List<UserDO> userDOs) {
        throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
    }

    @Override
    public int batchUpdate(@NonNull List<UserDO> userDOs) {
        return userDOs.stream().map(userDOMapper::updateById).reduce(0, Integer::sum);
    }

    @Override
    public Map<Integer, UserDO> batchGetByPrimaryIds(@NonNull Set<Integer> ids) {
        List<UserDO> userDOs = userDOMapper.selectBatchIds(ids);
        return Collect.toMap(userDOs, UserDO::getUserId);
    }

    @Override
    public UserDO merge(@NonNull UserDO priority, @NonNull UserDO original) {
        return Merger.INST.merge(priority, original);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Merger {

        Merger INST = Mappers.getMapper(Merger.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        UserDO merge(UserDO priority, @MappingTarget UserDO original);

    }
}
