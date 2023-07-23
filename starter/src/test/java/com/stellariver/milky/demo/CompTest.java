package com.stellariver.milky.demo;

import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.demo.adapter.controller.CompController;
import com.stellariver.milky.demo.adapter.controller.UserController;
import com.stellariver.milky.demo.client.po.LoginPO;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UserDOMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import lombok.CustomLog;
import org.junit.jupiter.api.Assertions;
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
    UserController userController;

    @Test
    public void testComp() throws InterruptedException {
        LoginPO loginPO = LoginPO.builder().userId("1000").password("admin").build();
        Result<String> loginResult = userController.login(loginPO);
        Assertions.assertNotNull(loginResult);
        Assertions.assertTrue(loginResult.getSuccess());
        Result<Void> result = compController.create(loginResult.getData(), 5);
        Thread.sleep(5000);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getSuccess());
    }

}
