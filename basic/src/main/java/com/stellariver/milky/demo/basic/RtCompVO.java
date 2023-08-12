package com.stellariver.milky.demo.basic;

import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.RtProcessorKey;
import com.stellariver.milky.demo.common.enums.NewBid;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RtCompVO {

    Integer roundId;
    MarketType marketType;
    TimeFrame timeFrame;
    Province province;
    List<PriceVO> prices = new ArrayList<>();
    List<MarketAsk> buyMarketAsks;
    List<MarketAsk> sellMarketAsks;
    List<Deal> deals;

    List<NewBid> buyBids;
    List<NewBid> sellBids;

    List<VolumeVO> buyVolumes;
    List<VolumeVO> sellVolumes;

    public RtCompVO(RtProcessorKey rtProcessorKey) {
        this.roundId = rtProcessorKey.getRoundId();
        this.marketType = rtProcessorKey.getMarketType();
        this.timeFrame = rtProcessorKey.getTimeFrame();
        this.province = rtProcessorKey.getProvince();
        buyVolumes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            double right = (i + 1) * 200D;
            if (i == 4) {
                right = 2000D;
            }
            VolumeVO volumeVO = new VolumeVO(i * 200D, 0D, right);
            buyVolumes.add(volumeVO);
        }

        sellVolumes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            double right = (i + 1) * 200D;
            if (i == 4) {
                right = 2000D;
            }
            VolumeVO volumeVO = new VolumeVO(i * 200D, 0D, right);
            sellVolumes.add(volumeVO);
        }

    }


}
