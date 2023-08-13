package com.stellariver.milky.demo.adapter.tunnel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.adapter.repository.domain.CompDODAOWrapper;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.adapter.websocket.WsHandler;
import com.stellariver.milky.demo.basic.CentralizedDeals;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Message;
import com.stellariver.milky.demo.basic.RtCompVO;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.domain.AbstractMetaUnit;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.common.RtProcessorKey;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.MarketSettingDO;
import com.stellariver.milky.demo.infrastructure.database.entity.MetaUnitDO;
import com.stellariver.milky.demo.infrastructure.database.entity.TieLinePowerDO;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TunnelImpl implements Tunnel {

    final MetaUnitDOMapper metaUnitDOMapper;
    final UnitDOMapper unitDOMapper;
    final CompDOMapper compDOMapper;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    final CompDODAOWrapper compDODAOWrapper;
    final MarketSettingMapper marketSettingMapper;

    @Override
    public List<Unit> listUnitsByCompId(Long compId) {
        LambdaQueryWrapper<UnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UnitDO::getCompId, compId);
        List<UnitDO> unitDOs = unitDOMapper.selectList(queryWrapper);
        return Collect.transfer(unitDOs, UnitDAOAdapter.Convertor.INST::to);
    }

    @Override
    public AbstractMetaUnit getByMetaUnitId(String metaUnitId) {
        return null;
    }

    @Override
    public long loadGeneratorNumber() {
        return 0;
    }

    @Override
    public long loadLoadNumber() {
        return 0;
    }

    @Override
    public long loadUnitNumber() {
        return 0;
    }

    @Override
    public Map<Integer, AbstractMetaUnit> getMetaUnitsByIds(Set<Integer> metaUnitIds) {
        LambdaQueryWrapper<MetaUnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MetaUnitDO::getMetaUnitId, metaUnitIds);
        Map<Integer, AbstractMetaUnit> metaUnitMap = new HashMap<>();
        metaUnitDOMapper.selectList(queryWrapper).forEach(metaUnitDO -> {
            AbstractMetaUnit abstractMetaUnit = UnitDAOAdapter.Convertor.INST.to(metaUnitDO);
            metaUnitMap.put(metaUnitDO.getMetaUnitId(), abstractMetaUnit);
        });
        return metaUnitMap;
    }

    @Override
    public Set<Integer> getMetaUnitIdBySourceIds(Set<Integer> metaUnitSourceIds) {
        LambdaQueryWrapper<MetaUnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MetaUnitDO::getSourceId, metaUnitSourceIds);
        return metaUnitDOMapper.selectList(queryWrapper).stream().map(MetaUnitDO::getMetaUnitId).collect(Collectors.toSet());
    }

    @Override
    public void push(Message message) {
        WsHandler.push(message.getUserId(), Json.toJson(message));
    }

    @Override
    @Nullable
    public Comp runningComp() {
        List<Comp> comps = compDODAOWrapper.memoryComps();
        Comp comp = comps.stream().max(Comparator.comparing(Comp::getCompId)).orElse(null);
        Map<RtProcessorKey, RtCompVO> rtCompVOMap = new HashMap<>();
        if (comp != null) {
            comp.getRtBidProcessors().forEach((pk, processor) -> rtCompVOMap.put(pk, processor.getRtCompVO()));
            comp.setRtCompVOMap(rtCompVOMap);
        }
        return comp;
    }

    @Override
    public void updateRoundIdForMarketSetting(Integer roundId) {
        MarketSettingDO marketSettingDO = new MarketSettingDO();
        marketSettingDO.setMarketSettingId(1);
        marketSettingDO.setRoundId(roundId + 1);
        marketSettingMapper.updateById(marketSettingDO);
    }

    final TieLineDOMapper tieLineDOMapper;
    @Override
    public void tieLinePower(Integer roundId, MarketType marketType, Map<TimeFrame, Double> replenishes, Map<TimeFrame, CentralizedDeals> centralizedDealsMap) {
        LambdaQueryWrapper<TieLinePowerDO> queryWrapper = new LambdaQueryWrapper<TieLinePowerDO>().eq(TieLinePowerDO::getRoundId, roundId);
        List<TieLinePowerDO> tieLinePowerDOs = tieLineDOMapper.selectList(queryWrapper);
        Map<Integer, TieLinePowerDO> map = Collect.toMapMightEx(tieLinePowerDOs, TieLinePowerDO::getPrd);
        replenishes.forEach((t, r) -> {
            Set<Integer> prds = new HashSet<>(t.getPrds());
            if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL) {
                prds.forEach(prd -> map.get(prd).setAnnualNonmarketTielinePower(r));
            } else if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
                prds.forEach(prd -> map.get(prd).setMonthlyTielinePower(r));
                prds.forEach(prd -> map.get(prd).setMonthlyMarketTielinePower(r));
                prds.forEach(prd -> map.get(prd).setMonthlyNonmarketTielinePower(r));
            } else if (marketType == MarketType.INTER_SPOT_PROVINCIAL) {
                prds.forEach(prd -> map.get(prd).setDaTielinePower(r));
                prds.forEach(prd -> map.get(prd).setDaMarketTielinePower(r));
                prds.forEach(prd -> map.get(prd).setDaNonmarketTielinePower(r));
            } else {
                throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
            }
        });

        centralizedDealsMap.forEach((t, r) -> {
            Set<Integer> prds = new HashSet<>(t.getPrds());
            if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL) {
                prds.forEach(prd -> map.get(prd).setAnnualMarketTielinePower(r.getDealQuantityTotal()));

                prds.forEach(prd -> {
                    double v = map.get(prd).getAnnualMarketTielinePower() + map.get(prd).getAnnualNonmarketTielinePower();
                    map.get(prd).setAnnualTielinePower(v);
                });
            } else if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
                prds.forEach(prd -> map.get(prd).setMonthlyMarketTielinePower(r.getDealQuantityTotal()));
                prds.forEach(prd -> {
                    double v = map.get(prd).getMonthlyMarketTielinePower() + map.get(prd).getMonthlyNonmarketTielinePower();
                    map.get(prd).setMonthlyTielinePower(v);
                });

            } else if (marketType == MarketType.INTER_SPOT_PROVINCIAL) {
                prds.forEach(prd -> map.get(prd).setDaTielinePower(r.getDealQuantityTotal()));
                prds.forEach(prd -> {
                    double v = map.get(prd).getDaMarketTielinePower() + map.get(prd).getDaNonmarketTielinePower();
                    map.get(prd).setDaTielinePower(v);
                });
            } else {
                throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
            }
        });


        tieLinePowerDOs.forEach(tieLineDOMapper::updateById);
    }

    @Override
    public void cast(Message message) {
        WsHandler.cast(message);
    }
}
