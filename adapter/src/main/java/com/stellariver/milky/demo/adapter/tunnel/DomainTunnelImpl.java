package com.stellariver.milky.demo.adapter.tunnel;

import com.stellariver.milky.demo.adapter.repository.domain.CompDAOAdapter;
import com.stellariver.milky.demo.adapter.repository.domain.PodDAOAdapter;
import com.stellariver.milky.demo.adapter.repository.domain.UserDAOAdapter;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Pod;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.tunnel.DomainTunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.entity.PodDO;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.PodDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.UserDOMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DomainTunnelImpl implements DomainTunnel {

    final PodDOMapper podDOMapper;
    final UserDOMapper userDOMapper;
    final CompDOMapper compDOMapper;

    @Override
    public Pod getByPodId(String podId) {
        PodDO podDO = podDOMapper.selectById(podId);
        return PodDAOAdapter.Convertor.INST.to(podDO);
    }

    @Override
    public Comp getByCompId(String compId) {
        CompDO compDO = compDOMapper.selectById(compId);
        return CompDAOAdapter.Convertor.INST.to(compDO);
    }


    @Override
    public User getByUserId(String userId) {
        UserDO userDO = userDOMapper.selectById(userId);
        return UserDAOAdapter.Convertor.INST.to(userDO);
    }

    @Override
    public boolean checkAdmin(String userId) {
        User user = getByUserId(userId);
        if (user == null) {
            return false;
        } else {
            return user.getRole() == Role.ADMIN;
        }
    }

}
