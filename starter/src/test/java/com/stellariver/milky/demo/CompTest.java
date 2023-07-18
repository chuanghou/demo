package com.stellariver.milky.demo;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.demo.adapter.controller.CompController;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import lombok.CustomLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@CustomLog
@SpringBootTest
public class CompTest {

    @Autowired
    private CompController compController;

    @Autowired
    private DomainTunnel domainTunnel;

    @Test
    public void testComp() throws InterruptedException {
        String token = TokenUtils.sign("101");
        Result<Void> result = compController.reset(5, token);
        Assertions.assertTrue(result.getSuccess());
        Comp comp = domainTunnel.getByAggregateId(Comp.class, "1");
        Assertions.assertEquals(comp.getCompStatus(), Status.CompStatus.CLOSE);
        result = compController.start(token);
        Assertions.assertTrue(result.getSuccess());
        comp = domainTunnel.getByAggregateId(Comp.class, "1");
        Assertions.assertEquals(comp.getCompStatus(), Status.CompStatus.OPEN);

        Throwable throwable = null;
        try {
            result = compController.start(token);
        } catch (BizEx bizEx) {
            throwable = bizEx;
        }
        Assertions.assertNotNull(throwable);
        Assertions.assertEquals("需要初始化项目", throwable.getMessage());
        comp = domainTunnel.getByAggregateId(Comp.class, "1");
        Assertions.assertEquals(comp.getRoundId(), 1);
        Assertions.assertEquals(comp.getMarketType(), MarketType.INTER_ANNUAL_PROVINCIAL);
        Assertions.assertEquals(comp.getMarketStatus(), Status.MarketStatus.OPEN);
        Thread.sleep(20_000);
        comp = domainTunnel.getByAggregateId(Comp.class, "1");
        Assertions.assertEquals(comp.getRoundId(), 1);
        Assertions.assertEquals(comp.getMarketType(), MarketType.INTER_ANNUAL_PROVINCIAL);
        Assertions.assertEquals(comp.getMarketStatus(), Status.MarketStatus.CLOSE);

    }

}
