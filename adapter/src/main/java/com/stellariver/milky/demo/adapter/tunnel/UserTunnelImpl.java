package com.stellariver.milky.demo.adapter.tunnel;

import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.tunnel.UserTunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UserDOMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTunnelImpl implements UserTunnel {

    final UserDOMapper userDOMapper;

    @Override
    public User getById(String userId) {
        UserDO userDO = userDOMapper.selectById(userId);
        return Convertor.INST.to(userDO);
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        User to(UserDO userDO);

    }
}
