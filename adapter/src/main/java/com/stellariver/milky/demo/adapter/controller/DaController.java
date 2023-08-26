package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.po.DaBidPO;
import com.stellariver.milky.demo.adapter.po.SectionPO;
import com.stellariver.milky.demo.adapter.vo.DaBidVO;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.GeneratorType;
import com.stellariver.milky.demo.basic.Label;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.common.NormalDaBid;
import com.stellariver.milky.demo.common.enums.UnitType;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.GeneratorMetaUnit;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.*;
import com.stellariver.milky.demo.infrastructure.database.mapper.*;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("da")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DaController {

    final UnitDOMapper unitDOMapper;
    final DomainTunnel domainTunnel;
    final SprMapper sprMapper;
    final Tunnel tunnel;
    final UnitInitStateMapper unitInitStateMapper;
    final GeneratorDOMapper generatorDOMapper;
    final DataController dataController;

    @GetMapping("costOfDa")
    public Map<Label, Map<Label, String>> costOfDa(@RequestHeader("token") String token) {
        Comp comp = tunnel.runningComp();
        BizEx.nullThrow(comp, ErrorEnums.PARAM_FORMAT_WRONG.message("当前无运行竞赛"));
        String userId = TokenUtils.getUserId(token);
        LambdaQueryWrapper<UnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UnitDO::getCompId, comp.getCompId());
        queryWrapper.eq(UnitDO::getUserId, userId);
        if (comp.getReview() == null) {
            queryWrapper.eq(UnitDO::getUserId, Integer.parseInt(userId));
        }
        queryWrapper.eq(UnitDO::getRoundId, comp.getRoundId());
        List<UnitDO> unitDOS = unitDOMapper.selectList(queryWrapper);
        List<Unit> units = Collect.transfer(unitDOS, UnitDAOAdapter.Convertor.INST::to);

        List<Unit> generatorUnits = units.stream().filter(unit -> unit.getMetaUnit().getUnitType() == UnitType.GENERATOR).collect(Collectors.toList());
        Map<Label, Map<Label, String>> map = new LinkedHashMap<>();


        Double renewableGovernmentSubsidy = sprMapper.selectList(null).get(0).getRenewableGovernmentSubsidy();
        for (Unit generatorUnit : generatorUnits) {
            Map<Label, String> subResult = new LinkedHashMap<>();
            GeneratorMetaUnit metaUnit = (GeneratorMetaUnit) generatorUnit.getMetaUnit();
            if (metaUnit.getGeneratorType() == GeneratorType.CLASSIC) {
                UnitInitState unitInitState = unitInitStateMapper.selectById(metaUnit.getSourceId());
                subResult.put(Label.initState, unitInitState.getInitState() ? "运行" : "停机");
                subResult.put(Label.initOutput, String.valueOf(unitInitState.getInitOutput()));
                subResult.put(Label.initHrUp, String.valueOf(unitInitState.getInitHrUp()));
                subResult.put(Label.initHrDn, String.valueOf(unitInitState.getInitHrDn()));

                String cost = dataController.costOfClassic(generatorUnit.getUnitId()).get(Label.costOfClassicOfAnnualAndMonthly_basic);
                subResult.put(Label.costOfClassicOfAnnualAndMonthly_basic, cost);

                GeneratorDO generatorDO = generatorDOMapper.selectById(metaUnit.getSourceId());
                subResult.put(Label.coldStartupCost, String.valueOf(generatorDO.getColdStartupCost()));
                subResult.put(Label.warmStartupCost, String.valueOf(generatorDO.getWarmStartupCost()));
                subResult.put(Label.hotStartupCost, String.valueOf(generatorDO.getHotStartupCost()));
                map.put(Label.classGeneratorCost, subResult);
            } else {
                subResult.put(Label.costOfRenewable, String.format("%.2f", (-1) * renewableGovernmentSubsidy));
                map.put(Label.renewableGeneratorCost, subResult);
            }
        }

        return map;
    }

    final RenewableUnitDOMapper renewableUnitDOMapper;
    final LoadDOMapper loadDOMapper;
    final LoadForecastMapper loadForecastMapper;

    @GetMapping("getDaBidVO")
    public DaBidVO getDaBidVO(@RequestParam Long unitId) {
        DaBidVO daBidVO = new DaBidVO();
        Unit unit = domainTunnel.getByAggregateId(Unit.class, unitId.toString());
        if (unit.getMetaUnit().getUnitType() == UnitType.GENERATOR) {
            GeneratorDO generatorDO = generatorDOMapper.selectById(unit.getMetaUnit().getSourceId());
            daBidVO.setMin(generatorDO.getMinP());
            daBidVO.setMax(daBidVO.getMax());
            List<Triple<Double, Double, Double>> triples = buildCostLine(unitId);
            daBidVO.setDaCostLines(triples);
            List<NormalDaBid> normalDaBids = unit.getNormalDaBids();
            daBidVO.setNormalDaBids(normalDaBids);
            GeneratorType generatorType = ((GeneratorMetaUnit) unit.getMetaUnit()).getGeneratorType();
            if (generatorType == GeneratorType.RENEWABLE) {
                daBidVO.setForecastBids(unit.getDaForecastBids());
                LambdaQueryWrapper<RenewableUnitDO> eq = new LambdaQueryWrapper<RenewableUnitDO>().eq(RenewableUnitDO::getUnitId, unit.getMetaUnit().getSourceId());
                List<Double> daForecasts = renewableUnitDOMapper.selectList(eq).stream()
                        .sorted(Comparator.comparing(RenewableUnitDO::getPrd)).map(RenewableUnitDO::getDaForecast).collect(Collectors.toList());
                daBidVO.setForecastQuantities(daForecasts);
            }
        } else {
            daBidVO.setForecastBids(unit.getDaForecastBids());
            LambdaQueryWrapper<LoadForecastDO> eq = new LambdaQueryWrapper<LoadForecastDO>().eq(LoadForecastDO::getLoadId, unit.getMetaUnit().getSourceId());
            List<Double> daForecasts = loadForecastMapper.selectList(eq).stream().sorted(Comparator.comparing(LoadForecastDO::getPrd)).map(LoadForecastDO::getDaForecast).collect(Collectors.toList());
            daBidVO.setForecastQuantities(daForecasts);
        }

        return daBidVO;
    }

    final ThermalUnitOperatingCostMapper costMapper;
    final StartupShutdownCostDOMapper startupShutdownCostDOMapper;

    @GetMapping("calculateSectionPrice")
    public Double calculateSectionPrice(@RequestBody SectionPO sectionPO) {
        List<Triple<Double, Double, Double>> triples = buildCostLine(sectionPO.getUnitId());
        double volume = 0D;
        for (Triple<Double, Double, Double> triple : triples) {
            if (triple.getRight() <= sectionPO.getLeft()) {
                continue;
            }
            if (triple.getLeft() >= sectionPO.getRight()) {
                continue;
            }
            double maxLeft = Math.max(sectionPO.getLeft(), triple.getLeft());
            double maxRight = Math.min(sectionPO.getRight(), triple.getRight());
            volume += (maxRight - maxLeft) * triple.getMiddle();
        }
        return volume * sectionPO.getRatio() /(sectionPO.getRight() - sectionPO.getLeft());
    }


    private List<Triple<Double, Double, Double>> buildCostLine(Long unitId) {
        List<Triple<Double, Double, Double>> triples = new ArrayList<>();
        Unit unit = domainTunnel.getByAggregateId(Unit.class, unitId.toString());
        Integer sourceId = unit.getMetaUnit().getSourceId();
        LambdaQueryWrapper<ThermalUnitOperatingCost> eq = new LambdaQueryWrapper<ThermalUnitOperatingCost>().eq(ThermalUnitOperatingCost::getUnitId, sourceId);
        List<ThermalUnitOperatingCost> thermalUnitOperatingCosts = costMapper.selectList(eq);
        GeneratorMetaUnit metaUnit = (GeneratorMetaUnit) unit.getMetaUnit();

        if (metaUnit.getGeneratorType() == GeneratorType.CLASSIC) {
            Double minP = generatorDOMapper.selectById(sourceId).getMinP();
            StartupShutdownCostDO startupShutdownCostDO = startupShutdownCostDOMapper.selectById(sourceId);
            double basePrice = startupShutdownCostDO.getSpotCostMinoutput() / minP;
            double left = 0D;
            double right = minP;
            Triple<Double, Double, Double> triple = Triple.of(left, right, basePrice);
            triples.add(triple);
            for (ThermalUnitOperatingCost thermalUnitOperatingCost : thermalUnitOperatingCosts) {
                left = right;
                right = right + thermalUnitOperatingCost.getSpotCostMw();
                triple = Triple.of(left, thermalUnitOperatingCost.getSpotCostMarginalCost(), right);
                triples.add(triple);
            }
        } else {
            GeneratorDO generatorDO = generatorDOMapper.selectById(sourceId);
            Double renewableGovernmentSubsidy = sprMapper.selectList(null).get(0).getRenewableGovernmentSubsidy();
            triples.add(Triple.of(generatorDO.getMinP(), renewableGovernmentSubsidy, generatorDO.getMaxP()));
        }

        return triples;
    }

    final IntraDaOfferMapper intraDaOfferMapper;
    final InterDaOfferMapper interDaOfferMapper;
    final IntraDaGeneratorForecastBidMapper generatorBidMapper;
    final IntraDaLoadForecastBidMapper loadBidMapper;
    final MarketSettingMapper marketSettingMapper;

    @PostMapping("submitDaBid")
    public Result<Void> submitDaBid(@RequestBody DaBidPO daBidPO) {

        UnitCommand.DaBidDeclare command = UnitCommand.DaBidDeclare.builder()
                .normalDaBids(daBidPO.getNormalDaBids())
                .daForecastBids(daBidPO.getForecastDaBids())
                .unitId(daBidPO.getUnitId())
                .build();
        CommandBus.accept(command, new HashMap<>());
        int dbRoundId = tunnel.runningComp().getRoundId() + 1;
        Unit unit = domainTunnel.getByAggregateId(Unit.class, daBidPO.getUnitId().toString());
        Integer sourceId = unit.getMetaUnit().getSourceId();

        if (Collect.isNotEmpty(daBidPO.getNormalDaBids())) {
            LambdaQueryWrapper<IntraDaUO> intraDaUOLambdaQueryWrapper = new LambdaQueryWrapper<>();
            intraDaUOLambdaQueryWrapper.eq(IntraDaUO::getRoundId, dbRoundId);
            intraDaUOLambdaQueryWrapper.eq(IntraDaUO::getUnitId, sourceId);
            for (int i = 0; i < daBidPO.getNormalDaBids().size(); i++) {
                NormalDaBid normalDaBid = daBidPO.getNormalDaBids().get(i);
                IntraDaUO intraDaUO = new IntraDaUO();
                intraDaUO.setRoundId(dbRoundId);
                intraDaUO.setUnitId(sourceId);
                intraDaUO.setOfferId(i + 1);
                intraDaUO.setOfferMw(normalDaBid.getRight() - normalDaBid.getLeft());
                intraDaUO.setOfferPrice(normalDaBid.getPrice());
                intraDaOfferMapper.update(intraDaUO, intraDaUOLambdaQueryWrapper);
            }
        }

        if (Collect.isNotEmpty(daBidPO.getForecastDaBids())) {
            if (unit.getMetaUnit().getUnitType() == UnitType.GENERATOR) {
                for (int i = 0; i < daBidPO.getForecastDaBids().size(); i++) {
                    IntraDaGeneratorForecastBid intraDaGeneratorForecastBid = new IntraDaGeneratorForecastBid();
                    LambdaQueryWrapper<IntraDaGeneratorForecastBid> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(IntraDaGeneratorForecastBid::getRoundId, dbRoundId);
                    queryWrapper.eq(IntraDaGeneratorForecastBid::getUnitId, sourceId);
                    queryWrapper.eq(IntraDaGeneratorForecastBid::getPrd, i);
                    intraDaGeneratorForecastBid.setForecastMw(daBidPO.getForecastDaBids().get(i));
                    generatorBidMapper.update(intraDaGeneratorForecastBid, queryWrapper);
                }
            } else {
                MarketSettingDO marketSettingDO = marketSettingMapper.selectById(1);
                Double bidPriceCap = marketSettingDO.getBidPriceCap();
                Integer spotNumBidSegs = marketSettingDO.getSpotNumBidSegs();
                for (int i = 0; i < daBidPO.getForecastDaBids().size(); i++) {
                    IntraDaLoadForecastBid intraDaLoadForecastBid = new IntraDaLoadForecastBid();
                    intraDaLoadForecastBid.setBidPrice(bidPriceCap);
                    double mw = daBidPO.getForecastDaBids().get(i) / spotNumBidSegs;
                    intraDaLoadForecastBid.setBidMw(mw);
                    for (int j = 0; j < spotNumBidSegs; j++) {
                        LambdaQueryWrapper<IntraDaLoadForecastBid> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(IntraDaLoadForecastBid::getRoundId, dbRoundId);
                        queryWrapper.eq(IntraDaLoadForecastBid::getLoadId, sourceId);
                        queryWrapper.eq(IntraDaLoadForecastBid::getPrd, i);
                        queryWrapper.eq(IntraDaLoadForecastBid::getBidId, j + 1);
                        loadBidMapper.update(intraDaLoadForecastBid, queryWrapper);
                    }
                }
            }
        }

        return Result.success();
    }

}
