package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.common.tool.util.StreamMap;
import com.stellariver.milky.demo.basic.AgentConfig;
import com.stellariver.milky.demo.basic.BasicConvertor;
import com.stellariver.milky.demo.common.MarketStatus;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.domain.support.dependency.DaoAdapter;
import com.stellariver.milky.domain.support.dependency.DataObjectInfo;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * @author houchuang
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompDAOAdapter implements DaoAdapter<Comp> {

    @Override
    public Comp toAggregate(@NonNull Object dataObject) {
        CompDO compDO = (CompDO) dataObject;

        Duration duration1 = Duration.of(compDO.getInterprovincialAnnualBidDuration(), ChronoUnit.SECONDS);
        Duration duration2 = Duration.of(compDO.getIntraprovincialAnnualBidDuration(), ChronoUnit.SECONDS);
        Duration duration3 = Duration.of(compDO.getInterprovincialMonthlyBidDuration(), ChronoUnit.SECONDS);
        Duration duration4 = Duration.of(compDO.getIntraprovincialMonthlyBidDuration(), ChronoUnit.SECONDS);
        Duration duration5 = Duration.of(compDO.getIntraprovincialSpotBidDuration(), ChronoUnit.SECONDS);
        Duration duration6 = Duration.of(compDO.getInterprovincialSpotBidDuration(), ChronoUnit.SECONDS);

        Map<MarketType, Duration> marketTypeDuration = StreamMap.<MarketType, Duration>init()
                .put(MarketType.INTER_ANNUAL_PROVINCIAL, duration1)
                .put(MarketType.INTRA_ANNUAL_PROVINCIAL, duration2)
                .put(MarketType.INTER_MONTHLY_PROVINCIAL, duration3)
                .put(MarketType.INTRA_MONTHLY_PROVINCIAL, duration4)
                .put(MarketType.INTRA_SPOT_PROVINCIAL, duration5)
                .put(MarketType.INTER_SPOT_PROVINCIAL, duration6)
                .getMap();

        return Comp.builder()
                .compId(compDO.getMarketSettingId())
                .roundId(compDO.getRoundId())
                .roundNum(compDO.getRoundNum())
                .marketType(Kit.enumOfMightEx(MarketType::getDbCode, compDO.getMarketType()))
                .agentConfigs(Json.parseList(compDO.getAgentConfig(), AgentConfig.class))
                .durationMap(marketTypeDuration)
                .limitations(new HashMap<>())
                .marketStatus(MarketStatus.valueOf(compDO.getMarketStatus()))
                .version(compDO.getVersion())
                .replenishMap(new HashMap<>())
                .build();
    }

    @Override
    public Object toDataObject(Comp comp, DataObjectInfo dataObjectInfo) {
        return CompDO.builder()
                .marketSettingId(comp.getCompId())
                .roundId(comp.getRoundId())
                .marketType(comp.getMarketType().getDbCode())
                .marketStatus(comp.getMarketStatus().name())
                .version(comp.getVersion())
                .build();
    }

    @Override
    public DataObjectInfo dataObjectInfo(String aggregateId) {
        return DataObjectInfo.builder().clazz(CompDO.class).primaryId(Integer.parseInt(aggregateId)).build();
    }

}
