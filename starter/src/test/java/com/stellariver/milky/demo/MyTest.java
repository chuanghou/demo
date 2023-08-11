package com.stellariver.milky.demo;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.basic.MarketAsk;
import com.stellariver.milky.demo.basic.RtCompVO;
import com.stellariver.milky.demo.client.vo.BalanceVO;
import com.stellariver.milky.demo.client.vo.BidVO;
import com.stellariver.milky.demo.client.vo.DealVO;
import com.stellariver.milky.demo.client.vo.UnitVO;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.enums.*;
import com.stellariver.milky.demo.domain.RtProcessorKey;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyTest {

    @Test
    public void sss() {
        Map<RtProcessorKey, RtCompVO> rtCompVOMap = new HashMap<>();
        RtProcessorKey processorKey = RtProcessorKey.builder().roundId(0).timeFrame(TimeFrame.PEAK).marketType(MarketType.INTER_ANNUAL_PROVINCIAL).province(Province.TRANSFER).build();
        NewBid newBid = NewBid.builder().bidId(11L).direction(Direction.BUY).price(100D).quantity(100D).marketType(MarketType.INTER_ANNUAL_PROVINCIAL).province(Province.TRANSFER).build();
        Deal deal = Deal.builder().date(new Date()).dealId(1111L).unitId(344L).price(3455D).quantity(12432423D).build();
        MarketAsk marketAsk = MarketAsk.builder().quantity(123D).price(24D).build();

        RtCompVO build = RtCompVO.builder()
                .buyBids(Arrays.asList(newBid, newBid))
                .currentPrice(345D)
                .sellBids(Arrays.asList(newBid, newBid))
                .deals(Arrays.asList(deal, deal))
                .buyMarketAsks(Arrays.asList(marketAsk, marketAsk))
                .sellMarketAsks(Arrays.asList(marketAsk, marketAsk))
                .build();
        rtCompVOMap.put(processorKey, build);
        System.out.println(Json.toJson(rtCompVOMap));

        BalanceVO balanceVO0 = BalanceVO.builder().direction(Direction.BUY).balance(100D).build();
        BalanceVO balanceVO1 = BalanceVO.builder().direction(Direction.SELL).balance(100D).build();

        DealVO dealVO = DealVO.builder().status("成交")
                .date(new Date())
                .price(100D)
                .quantity(100D)
                .build();

        BidVO build1 = BidVO.builder().cancelable(true)
                .notDeal(100D)
                .price(100D)
                .quantity(100D)
                .dealVOs(Collect.asList(dealVO, dealVO))
                .build();

        UnitVO unitVO = UnitVO.builder().unitType(UnitType.GENERATOR)
                .unitId(19L)
                .name("xxx机组")
                .timeFrame(TimeFrame.PEAK)
                .unitType(UnitType.GENERATOR)
                .province(Province.TRANSFER)
                .balanceVOs(Arrays.asList(balanceVO0, balanceVO1))
                .bidVOs(Collect.asList(build1, build1))
                .build();

        System.out.println(Json.toJson(unitVO));
    }
}
