package com.stellariver.milky.demo;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.demo.adapter.controller.CompController;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.domain.Comp;
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

    @Test
    public void testComp() throws InterruptedException {

        Result<Void> result = compController.init(5);
        Assertions.assertTrue(result.getSuccess());

        result = compController.start();
        Assertions.assertTrue(result.getSuccess());

        Throwable throwable = null;
        try {
            result = compController.start();
        } catch (BizEx bizEx) {
            throwable = bizEx;
        }
        Assertions.assertNotNull(throwable);
        Assertions.assertEquals("应该首先初始化项目", throwable.getMessage());
        Comp comp = domainTunnel.getByAggregateId(Comp.class, "1");
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
