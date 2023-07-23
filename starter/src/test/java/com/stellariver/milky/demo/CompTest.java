package com.stellariver.milky.demo;

import com.stellariver.milky.demo.adapter.controller.CompController;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UserDOMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import lombok.CustomLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CustomLog
@SpringBootTest
public class CompTest {

    @Autowired
    private CompController compController;

    @Autowired
    private DomainTunnel domainTunnel;

    @Autowired
    UserDOMapper userDOMapper;

    @Test
    public void testComp() throws InterruptedException {
        UserDO userDO = userDOMapper.selectById(0);
        System.out.println(userDO);
    }

}
