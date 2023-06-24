package com.stellariver.milky.demo.adapter.tunnel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.demo.adapter.repository.domain.CompDAOAdapter;
import com.stellariver.milky.demo.adapter.repository.domain.PodDAOAdapter;
import com.stellariver.milky.demo.adapter.repository.domain.UserDAOAdapter;
import com.stellariver.milky.demo.basic.Stage;
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

import java.util.List;

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
    public Comp getCurrentComp() {
        LambdaQueryWrapper<CompDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(CompDO::getStage, Stage.END.name());
        List<CompDO> compDOS = compDOMapper.selectList(queryWrapper);
        if (compDOS.size() > 1) {
            throw new SysEx("系统存在多个运行中的竞赛，请联系管理员处理");
        } else if (compDOS.size() == 1) {
            return CompDAOAdapter.Convertor.INST.to(compDOS.get(0));
        }
        return null;
    }


    @Override
    public User getByUserId(String userId) {
        UserDO userDO = userDOMapper.selectById(userId);
        return UserDAOAdapter.Convertor.INST.to(userDO);
    }

}
