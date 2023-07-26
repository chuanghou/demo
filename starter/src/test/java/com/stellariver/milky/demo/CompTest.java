package com.stellariver.milky.demo;

import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.demo.adapter.controller.CompController;
import com.stellariver.milky.demo.adapter.controller.UserController;
import com.stellariver.milky.demo.client.po.LoginPO;
import com.stellariver.milky.demo.client.vo.LogInVO;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
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

    @Autowired
    CompDOMapper compDOMapper;

    @Test
    public void testComp() throws InterruptedException {
        compDOMapper.selectList(null).forEach(compDO -> compDOMapper.deleteById(compDO.getCompId()));
        LoginPO loginPO = LoginPO.builder().userId("1000").password("admin").build();
        Result<LogInVO> logInVO = userController.login(loginPO);
        Assertions.assertNotNull(logInVO);
        Assertions.assertTrue(logInVO.getSuccess());
        String token = logInVO.getData().getToken();
        compController.create(token, 5);
        Thread.sleep(1000);

        Result<Comp> compResult = compController.runningComp();
        Assertions.assertTrue(compResult.getSuccess());
        Comp runningComp = compResult.getData();
        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.INIT);
        compController.start(token, runningComp.getCompId());
        runningComp = compController.runningComp().getData();

        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.OPEN);
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        Thread.sleep(1100);

        runningComp = compController.runningComp().getData();
        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.OPEN);
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.CLOSE);

        Thread.sleep(1100);

        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.OPEN);
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTRA_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        Thread.sleep(1100);

        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.OPEN);
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTRA_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.CLOSE);

        compController.step(token, runningComp.getCompId());
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_MONTHLY_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        compController.step(token, runningComp.getCompId());
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_MONTHLY_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.CLOSE);

        compController.step(token, runningComp.getCompId());
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTRA_MONTHLY_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        compController.step(token, runningComp.getCompId());
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTRA_MONTHLY_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.CLOSE);

        compController.step(token, runningComp.getCompId());
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTRA_SPOT_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        compController.step(token, runningComp.getCompId());
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTRA_SPOT_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.CLOSE);

        compController.step(token, runningComp.getCompId());
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_SPOT_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        compController.step(token, runningComp.getCompId());
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_SPOT_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.CLOSE);

        compController.step(token, runningComp.getCompId());

        Assertions.assertSame(runningComp.getRoundId(), 1);
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        compController.step(token, runningComp.getCompId());
        Assertions.assertSame(runningComp.getRoundId(), 1);
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.CLOSE);

    }


}
