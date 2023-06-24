package com.stellariver.milky.demo.domain.tunnel;

import com.stellariver.milky.demo.domain.Pod;

public interface PodTunnel {

    Pod queryById(String podId);

}
