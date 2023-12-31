package com.stellariver.milky.demo.basic;

import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@lombok.Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stage {

    Integer roundId;
    MarketType marketType;
    Status.MarketStatus marketStatus;

    public Stage next() {
        Status.MarketStatus nextMarketStatus = this.marketStatus.opposite();
        Integer nextRoundId = this.roundId;
        MarketType nextMarketType = this.marketType;

        if (nextMarketStatus == Status.MarketStatus.OPEN) {
            int nextDbCode = (marketType.getDbCode() + 1)%MarketType.values().length;
            nextMarketType = Kit.enumOfMightEx(MarketType::getDbCode, nextDbCode);
        } else if (nextMarketType == MarketType.FINAL_CLEAR) {
            nextMarketStatus = Status.MarketStatus.OPEN;
            nextMarketType = MarketType.INTER_ANNUAL_PROVINCIAL;
            nextRoundId += 1;
        }

        return Stage.builder().roundId(nextRoundId).marketType(nextMarketType).marketStatus(nextMarketStatus).build();
    }

    public boolean laterThan(Stage stage) {

        if (roundId > stage.getRoundId()) {
            return true;
        } else if (roundId < stage.getRoundId()) {
            return false;
        }

        if (marketType.getDbCode() > stage.getMarketType().getDbCode()) {
            return true;
        } else if (marketType.getDbCode() < stage.getMarketType().getDbCode()) {
            return false;
        }

        if (marketStatus == stage.getMarketStatus()) {
            return false;
        } else {
            return marketStatus != Status.MarketStatus.OPEN || stage.getMarketStatus() != Status.MarketStatus.CLOSE;
        }

    }

    public boolean lastOne(Integer roundTotal) {
        boolean eq0 = Kit.eq(roundTotal - 1, roundId);
        boolean eq1 = marketType == MarketType.FINAL_CLEAR;
        return eq0 && eq1;
    }

}
