package com.stellariver.milky.demo.adapter.tunnel;

import com.stellariver.milky.demo.adapter.repository.domain.PodDAOAdapter;
import com.stellariver.milky.demo.domain.Pod;
import com.stellariver.milky.demo.domain.tunnel.PodTunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.PodDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.PodDOMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PodTunnelImpl implements PodTunnel {

    final PodDOMapper podDOMapper;

    @Override
    public Pod queryById(String podId) {
        PodDO podDO = podDOMapper.selectById(podId);
        return Convertor.INST.to(podDO);
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Pod to(PodDO podDO);

    }
}
