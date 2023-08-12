package com.stellariver.milky.demo;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.basic.MarketAsk;
import com.stellariver.milky.demo.basic.PriceVO;
import com.stellariver.milky.demo.basic.RtCompVO;
import com.stellariver.milky.demo.basic.VolumeVO;
import com.stellariver.milky.demo.client.vo.BalanceVO;
import com.stellariver.milky.demo.client.vo.BidVO;
import com.stellariver.milky.demo.client.vo.DealVO;
import com.stellariver.milky.demo.client.vo.UnitVO;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.enums.*;
import com.stellariver.milky.demo.common.RtProcessorKey;
import org.junit.jupiter.api.Test;

import java.util.*;

public class MyTest {

    @Test
    public void sss() {
        NewBid newBid = NewBid.builder().bidId(11L).direction(Direction.BUY).price(100D).quantity(100D).marketType(MarketType.INTER_ANNUAL_PROVINCIAL).province(Province.TRANSFER).build();
        Deal deal = Deal.builder().date(new Date()).dealId(1111L).unitId(344L).price(3455D).quantity(12432423D).build();
        MarketAsk marketAsk = MarketAsk.builder().quantity(123D).price(24D).build();
        PriceVO build2 = PriceVO.builder().price(34D).date(new Date()).build();

        VolumeVO volumeVO0 = VolumeVO.builder().left(0D).right(200D).value(222D).build();
        VolumeVO volumeVO1 = VolumeVO.builder().left(200D).right(400D).value(222D).build();
        VolumeVO volumeVO2 = VolumeVO.builder().left(400D).right(600D).value(222D).build();
        VolumeVO volumeVO3 = VolumeVO.builder().left(600D).right(800D).value(222D).build();
        VolumeVO volumeVO4 = VolumeVO.builder().left(6800D).right(2000D).value(222D).build();
        List<VolumeVO> list = Collect.asList(volumeVO0, volumeVO1, volumeVO2, volumeVO3, volumeVO4);

        RtCompVO build = RtCompVO.builder()
                .roundId(0)
                .marketType(MarketType.INTER_ANNUAL_PROVINCIAL)
                .province(Province.TRANSFER)
                .timeFrame(TimeFrame.PEAK)
                .buyBids(Arrays.asList(newBid, newBid))
                .prices(Arrays.asList(build2, build2))
                .sellBids(Arrays.asList(newBid, newBid))
                .deals(Arrays.asList(deal, deal))
                .buyMarketAsks(Arrays.asList(marketAsk, marketAsk))
                .sellMarketAsks(Arrays.asList(marketAsk, marketAsk))
                .buyVolumes(list)
                .sellVolumes(list)
                .build();
        System.out.println(Json.toJson(Arrays.asList(build, build)));


        BalanceVO balanceVO1 = BalanceVO.builder().capacity(1000D)
                .directions(Collect.asList(Direction.SELL)).onMatching(100D).dealed(100D).build();

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
                .balanceVO(balanceVO1)
                .bidVOs(Collect.asList(build1, build1))
                .build();

        System.out.println(Json.toJson(Arrays.asList(unitVO, unitVO)));
    }
}
