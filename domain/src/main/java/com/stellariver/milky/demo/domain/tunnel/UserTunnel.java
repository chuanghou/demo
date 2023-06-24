package com.stellariver.milky.demo.domain.tunnel;

import com.stellariver.milky.demo.domain.User;

public interface UserTunnel {

    User getById(String userId);

}
