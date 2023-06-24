package com.stellariver.milky.demo.domain.tunnel;

import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Pod;
import com.stellariver.milky.demo.domain.User;

import javax.annotation.Nullable;

public interface DomainTunnel {

    Pod getByPodId(String podId);

    User getByUserId(String userId);

    @Nullable
    Comp getCurrentComp();
}
