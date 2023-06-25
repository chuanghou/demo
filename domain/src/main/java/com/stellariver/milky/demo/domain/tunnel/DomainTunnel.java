package com.stellariver.milky.demo.domain.tunnel;

import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Pod;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.domain.User;

import javax.annotation.Nullable;
import java.util.List;

public interface DomainTunnel {

    Pod getByPodId(String podId);

    User getByUserId(String userId);

    boolean checkAdmin(String userId);

    Comp getByCompId(String compId);

    List<Unit> getBuyCompId(String compId);

}
