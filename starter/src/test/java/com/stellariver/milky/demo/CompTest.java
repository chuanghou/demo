package com.stellariver.milky.demo;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.Clock;
import com.stellariver.milky.demo.adapter.controller.CompController;
import com.stellariver.milky.demo.adapter.controller.UnitController;
import com.stellariver.milky.demo.adapter.controller.UserController;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.client.po.*;
import com.stellariver.milky.demo.client.vo.LogInVO;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.common.enums.*;
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

import java.util.*;
import java.util.stream.Collectors;

@CustomLog
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    public void testCompTemp() throws InterruptedException {
        compDOMapper.selectList(null).forEach(compDO -> compDOMapper.deleteById(compDO.getCompId()));
        LoginPO loginPO = LoginPO.builder().userId("1000").password("admin").build();
        Result<LogInVO> logInVO = userController.login(loginPO.getUserId(), loginPO.getPassword());
        Assertions.assertNotNull(logInVO);
        Assertions.assertTrue(logInVO.getSuccess());
        String token = logInVO.getData().getToken();
        List<Long> durationLengths = new ArrayList<>();
        for (int i = 0; i < MarketType.values().length; i++) {
            durationLengths.add(3L);
        }
        CompCreatePO compCreatePO = CompCreatePO.builder().durations(durationLengths).agentNumber(5).build();
        compController.create(token, compCreatePO);
        System.out.println("FIRST" + Clock.now());
        Thread.sleep(3100);
        System.out.println("SECOND" + Clock.now());
        Result<Comp> compResult = compController.runningComp();
        Assertions.assertTrue(compResult.getSuccess());
        Comp runningComp = compResult.getData();
        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.INIT);
        compController.start(token, runningComp.getCompId());
        runningComp = compController.runningComp().getData();
        Thread.sleep(1000000L);
    }

    @Test
    public void testComp() throws InterruptedException {
        compDOMapper.selectList(null).forEach(compDO -> compDOMapper.deleteById(compDO.getCompId()));
        LoginPO loginPO = LoginPO.builder().userId("1000").password("admin").build();
        Result<LogInVO> logInVO = userController.login(loginPO.getUserId(), loginPO.getPassword());
        Assertions.assertNotNull(logInVO);
        Assertions.assertTrue(logInVO.getSuccess());
        String token = logInVO.getData().getToken();
        List<Long> durationLengths = new ArrayList<>();
        for (int i = 0; i < MarketType.values().length; i++) {
            durationLengths.add(3L);
        }
        CompCreatePO compCreatePO = CompCreatePO.builder().durations(durationLengths).agentNumber(5).build();
        compController.create(token, compCreatePO);
        System.out.println( "FIRST" + Clock.now());
        Thread.sleep(3100);
        System.out.println( "SECOND" + Clock.now());
        Result<Comp> compResult = compController.runningComp();
        Assertions.assertTrue(compResult.getSuccess());
        Comp runningComp = compResult.getData();
        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.INIT);
        compController.start(token, runningComp.getCompId());
        runningComp = compController.runningComp().getData();

        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.OPEN);
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        System.out.println( "THREE" + Clock.now());
        Thread.sleep(3100);
        System.out.println( "FOUR" + Clock.now());

        runningComp = compController.runningComp().getData();
        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.OPEN);
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTER_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.CLOSE);

        System.out.println( "FIVE" + Clock.now());
        Thread.sleep(3100);
        System.out.println( "SIX" + Clock.now());

        Assertions.assertSame(runningComp.getCompStatus(), Status.CompStatus.OPEN);
        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTRA_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        Thread.sleep(3100);

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

        Assertions.assertSame(runningComp.getMarketType(), MarketType.FINAL_CLEAR);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

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
        Result<LogInVO> logInVO = userController.login(loginPO.getUserId(), loginPO.getPassword());
        Assertions.assertNotNull(logInVO);
        Assertions.assertTrue(logInVO.getSuccess());
        String adminToken = logInVO.getData().getToken();
        List<Long> durationLengths = new ArrayList<>();
        for (int i = 0; i < MarketType.values().length; i++) {
            durationLengths.add(600L);
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
        Long compId = compController.runningComp().getData().getCompId();
        Assertions.assertNotNull(compController.runningComp());

        compController.step(adminToken, runningComp.getCompId());

        Assertions.assertSame(runningComp.getMarketType(), MarketType.INTRA_ANNUAL_PROVINCIAL);
        Assertions.assertSame(runningComp.getMarketStatus(), Status.MarketStatus.OPEN);

        BidPO bidP0 = BidPO.builder()
                .timeFrame(TimeFrame.PEAK.name())
                .direction(Direction.SELL.name())
                .quantity(100D)
                .price(200D)
                .build();
        RealtimeNewBidPO realtimeNewBidPO0 = RealtimeNewBidPO.builder().bid(bidP0).unitId(transferGenerator.getUnitId()).build();
        unitController.realtimeNewBid(realtimeNewBidPO0, user0Token);

        Unit transLoad = user0Units.stream().filter(unit -> {
            boolean b0 = unit.getMetaUnit().getUnitType() == UnitType.LOAD;
            boolean b1 = unit.getMetaUnit().getProvince() == Province.TRANSFER;
            return b0 && b1;
        }).collect(Collectors.toList()).get(0);

        BidPO bidP1 = BidPO.builder()
                .timeFrame(TimeFrame.PEAK.name())
                .direction(Direction.BUY.name())
                .quantity(100D)
                .price(300D)
                .build();
        RealtimeNewBidPO realtimeNewBidPO1 = RealtimeNewBidPO.builder().bid(bidP1).unitId(transLoad.getUnitId()).build();
        unitController.realtimeNewBid(realtimeNewBidPO1, user0Token);

        Thread.sleep(1000L);

        Unit unit = domainTunnel.getByAggregateId(Unit.class, transLoad.getUnitId().toString());
        Assertions.assertEquals(unit.getBids().size(), 1);
        Bid bid = new ArrayList<>(unit.getBids().values()).get(0);
        Deal deal = bid.getDeals().get(0);
        Assertions.assertEquals(deal.getQuantity(), 100D);
        Assertions.assertEquals(deal.getPrice(), 200D);
        System.out.println("Hello");

        BidPO bidP2 = BidPO.builder()
                .timeFrame(TimeFrame.PEAK.name())
                .direction(Direction.BUY.name())
                .quantity(200D)
                .price(300D)
                .build();
        RealtimeNewBidPO realtimeNewBidPO2 = RealtimeNewBidPO.builder().bid(bidP2).unitId(transLoad.getUnitId()).build();
        unitController.realtimeNewBid(realtimeNewBidPO2, user0Token);

        BidPO bidP3 = BidPO.builder()
                .timeFrame(TimeFrame.PEAK.name())
                .direction(Direction.SELL.name())
                .quantity(100D)
                .price(200D)
                .build();
        RealtimeNewBidPO realtimeNewBidPO3 = RealtimeNewBidPO.builder().bid(bidP3).unitId(transferGenerator.getUnitId()).build();
        unitController.realtimeNewBid(realtimeNewBidPO3, user0Token);

        transferGenerator = domainTunnel.getByAggregateId(Unit.class, transferGenerator.getUnitId().toString());
        transLoad = domainTunnel.getByAggregateId(Unit.class, transLoad.getUnitId().toString());

        bid = transLoad.getBids().values().stream().max(Comparator.comparing(Bid::getDate)).orElseThrow(() -> new BizEx(ErrorEnums.UNREACHABLE_CODE));
        Assertions.assertEquals(bid.getDeals().size(), 1);
        deal = bid.getDeals().get(0);

        Assertions.assertEquals(deal.getPrice(), 300D);
        Assertions.assertEquals(deal.getQuantity(), 100D);
        RealtimeCancelBidPO realtimeCancelBidPO = RealtimeCancelBidPO.builder().bidId(bid.getBidId()).unitId(transLoad.getUnitId()).build();
        unitController.realtimeCancelBid(realtimeCancelBidPO, user0Token);
        Thread.sleep(1000L);
        transLoad = domainTunnel.getByAggregateId(Unit.class, transLoad.getUnitId().toString());
        bid = transLoad.getBids().values().stream().max(Comparator.comparing(Bid::getDate)).orElseThrow(() -> new BizEx(ErrorEnums.UNREACHABLE_CODE));
        Assertions.assertEquals(bid.getBidStatus(), BidStatus.CANCELLED);

        Double balance = transLoad.getBalances().get(TimeFrame.PEAK).get(Direction.BUY);
        double v = balance + 200D;
        Assertions.assertEquals(v, transLoad.getMetaUnit().getCapacity().get(TimeFrame.PEAK).get(Direction.BUY));

        BidPO bidP4 = BidPO.builder()
                .timeFrame(TimeFrame.FLAT.name())
                .direction(Direction.BUY.name())
                .quantity(100D)
                .price(300D)
                .build();
        RealtimeNewBidPO realtimeNewBidPO4 = RealtimeNewBidPO.builder().bid(bidP4).unitId(transLoad.getUnitId()).build();
        unitController.realtimeNewBid(realtimeNewBidPO4, user0Token);
        transLoad = domainTunnel.getByAggregateId(Unit.class, transLoad.getUnitId().toString());
        balance = transLoad.getBalances().get(TimeFrame.FLAT).get(Direction.BUY);
        Assertions.assertEquals(balance, 200D);
        Result<Void> step = compController.step(adminToken, compId);
        Thread.sleep(1000L);
        transLoad = domainTunnel.getByAggregateId(Unit.class, transLoad.getUnitId().toString());
        balance = transLoad.getBalances().get(TimeFrame.FLAT).get(Direction.BUY);
        Assertions.assertEquals(balance, 300D);

    }


}
