package com.stellariver.milky.demo;

import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.demo.adapter.controller.CompController;
import com.stellariver.milky.demo.adapter.controller.UnitController;
import com.stellariver.milky.demo.adapter.controller.UserController;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.client.po.BidPO;
import com.stellariver.milky.demo.client.po.CentralizedBidPO;
import com.stellariver.milky.demo.client.po.CompCreatePO;
import com.stellariver.milky.demo.client.po.LoginPO;
import com.stellariver.milky.demo.client.vo.LogInVO;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.domain.support.ErrorEnums;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    UnitController unitController;

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
        List<Long> durationLengths = new ArrayList<>();
        for (int i = 0; i < MarketType.values().length; i++) {
            durationLengths.add(1L);
        }
        CompCreatePO compCreatePO = CompCreatePO.builder().durations(durationLengths).agentNumber(5).build();
        compController.create(token, compCreatePO);
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


    @Test
    @SneakyThrows
    public void testCentralBids() {
        compDOMapper.selectList(null).forEach(compDO -> compDOMapper.deleteById(compDO.getCompId()));
        LoginPO loginPO = LoginPO.builder().userId("1000").password("admin").build();
        Result<LogInVO> logInVO = userController.login(loginPO);
        Assertions.assertNotNull(logInVO);
        Assertions.assertTrue(logInVO.getSuccess());
        String adminToken = logInVO.getData().getToken();
        List<Long> durationLengths = new ArrayList<>();
        for (int i = 0; i < MarketType.values().length; i++) {
            durationLengths.add(60L);
        }
        CompCreatePO compCreatePO = CompCreatePO.builder().durations(durationLengths).agentNumber(5).build();
        compController.create(adminToken, compCreatePO);
        Thread.sleep(10);

        Result<Comp> compResult = compController.runningComp();
        Assertions.assertTrue(compResult.getSuccess());
        Comp runningComp = compResult.getData();
        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.INIT);
        compController.start(adminToken, runningComp.getCompId());
        Thread.sleep(100);
        runningComp = compController.runningComp().getData();
        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.OPEN);
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        String user0Token = TokenUtils.sign("0");
        List<Unit> user0Units = unitController.listUnits(runningComp.getCompId(), user0Token).getData();

        String user1Token = TokenUtils.sign("1");
        List<Unit> user1Units = unitController.listUnits(runningComp.getCompId(), user1Token).getData();

        String user2Token = TokenUtils.sign("2");
        List<Unit> user2Units = unitController.listUnits(runningComp.getCompId(), user2Token).getData();

        String user3Token = TokenUtils.sign("3");
        List<Unit> user3Units = unitController.listUnits(runningComp.getCompId(), user3Token).getData();

        String user4Token = TokenUtils.sign("4");
        List<Unit> user4Units = unitController.listUnits(runningComp.getCompId(), user4Token).getData();


        List<Unit> units0 = user0Units.stream().filter(unit -> {
            boolean b0 = unit.getMetaUnit().getUnitType() == UnitType.GENERATOR;
            boolean b1 = unit.getMetaUnit().getProvince() == Province.TRANSFER;
            return b0 && b1;
        }).collect(Collectors.toList());
        SysEx.trueThrow(units0.size() != 1, ErrorEnums.SYS_EX);
        Unit transferGenerator = units0.get(0);

        for (TimeFrame timeFrame : TimeFrame.values()) {
            Map<Direction, Double> balances = transferGenerator.getBalances().get(timeFrame);
            Double sellBalance = balances.get(Direction.SELL);
            double quantity = sellBalance / 3;
            BidPO bidPO = BidPO.builder().direction(Direction.SELL.name()).price(100D).quantity(quantity).timeFrame(timeFrame.name()).build();
            CentralizedBidPO centralizedBidPO = CentralizedBidPO.builder().unitId(transferGenerator.getUnitId()).bids(Arrays.asList(bidPO, bidPO, bidPO)).build();
            unitController.centralizedBid(centralizedBidPO, user0Token);
        }


        List<Unit> units1 = user0Units.stream().filter(unit -> {
            boolean b0 = unit.getMetaUnit().getUnitType() == UnitType.LOAD;
            boolean b1 = unit.getMetaUnit().getProvince() == Province.RECEIVER;
            return b0 && b1;
        }).collect(Collectors.toList());
        SysEx.trueThrow(units1.size() != 1, ErrorEnums.SYS_EX);
        Unit receiveLoad = units1.get(0);

        for (TimeFrame timeFrame : TimeFrame.values()) {
            Map<Direction, Double> balances = receiveLoad.getBalances().get(timeFrame);
            Double sellBalance = balances.get(Direction.BUY);
            double quantity = sellBalance / 3;
            BidPO bidPO = BidPO.builder().direction(Direction.BUY.name()).price(100D).quantity(quantity).timeFrame(timeFrame.name()).build();
            CentralizedBidPO centralizedBidPO = CentralizedBidPO.builder().unitId(receiveLoad.getUnitId()).bids(Arrays.asList(bidPO, bidPO, bidPO)).build();
            unitController.centralizedBid(centralizedBidPO, user0Token);
        }

        compController.step(adminToken, runningComp.getCompId());


    }


}
