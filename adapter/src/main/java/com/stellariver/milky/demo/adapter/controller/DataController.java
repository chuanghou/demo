package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.Enumeration;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Label;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.Round;
import com.stellariver.milky.demo.domain.AbstractMetaUnit;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.infrastructure.database.entity.*;
import com.stellariver.milky.demo.infrastructure.database.mapper.*;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
@RequiredArgsConstructor
@RequestMapping("dataController")
public class DataController {

    final SprMapper sprMapper;
    final TpbfsdMapper tpbfsdMapper;


    @GetMapping("listMarketTypes")
    public List<Enumeration> listMarketTypes() {
        return Arrays.stream(MarketType.values()).map(e -> new Enumeration(e.name(), e.getDesc())).collect(Collectors.toList());
    }

    @GetMapping("listLabels")
    public List<Enumeration> listLabels() {
        return Arrays.stream(Label.values()).map(e -> new Enumeration(e.name(), e.getDesc())).collect(Collectors.toList());
    }

    @GetMapping("systemParameterRelease")
    Map<String, Map<String, List<Double>>> systemParameterRelease(@RequestParam String marketTypeValue) {

        MarketType marketType = MarketType.valueOf(marketTypeValue);

        List<SprDO> sprDOs = sprMapper.selectList(null);

        Map<String, Map<String, List<Double>>> result = new HashMap<>();

        List<SprDO> transferSprDOs = sprDOs.stream()
                .filter(d -> Kit.eq(d.getProv(), Province.TRANSFER.getDbCode()))
                .sorted(Comparator.comparing(SprDO::getDt))
                .collect(Collectors.toList());

        Map<String, List<Double>> transferData = new HashMap<>();

        transferData.put(Label.min_thermal_mw.name(), Collect.transfer(transferSprDOs, SprDO::getMinThermalMw));
        transferData.put(Label.adjustable_thermal_mw.name(), Collect.transfer(transferSprDOs, SprDO::getAdjustableThermalMw));

        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            transferData.put(Label.annual_renewable_forecast.name(), Collect.transfer(transferSprDOs, SprDO::getAnnualRenewableForecast));
            transferData.put(Label.annual_load_forecast.name(), Collect.transfer(transferSprDOs, SprDO::getAnnualLoadForecast));
        } else if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL || marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            transferData.put(Label.annual_renewable_forecast.name(), Collect.transfer(transferSprDOs, SprDO::getDaRenewableForecast));
            transferData.put(Label.annual_load_forecast.name(), Collect.transfer(transferSprDOs, SprDO::getMonthlyLoadForecast));
        } else if (marketType == MarketType.INTRA_SPOT_PROVINCIAL || marketType == MarketType.INTER_SPOT_PROVINCIAL) {
            transferData.put(Label.annual_renewable_forecast.name(), Collect.transfer(transferSprDOs, SprDO::getDaRenewableForecast));
            transferData.put(Label.annual_load_forecast.name(), Collect.transfer(transferSprDOs, SprDO::getDaLoadForecast));
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }

        LambdaQueryWrapper<TpbfsdDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TpbfsdDO::getRoundId, Round.ONE.getDbCode());
        queryWrapper.eq(TpbfsdDO::getProv, Province.TRANSFER.getDbCode());

        List<TpbfsdDO> transferTpbfsdDOS = tpbfsdMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(TpbfsdDO::getPrd))
                .collect(Collectors.toList());

        transferData.put(Label.annual_receive_forecast_mw.name(), Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getAnnualReceivingForecastMw));

        result.put(Label.annual_receive_forecast_mw.name(), transferData);


        List<SprDO> receiveSprDOs = sprDOs.stream()
                .filter(d -> Kit.eq(d.getProv(), Province.RECEIVER.getDbCode()))
                .sorted(Comparator.comparing(SprDO::getDt))
                .collect(Collectors.toList());

        Map<String, List<Double>> receiveData = new HashMap<>();
        receiveData.put(Label.min_thermal_mw.name(), Collect.transfer(receiveSprDOs, SprDO::getMinThermalMw));
        receiveData.put(Label.adjustable_thermal_mw.name(), Collect.transfer(receiveSprDOs, SprDO::getAdjustableThermalMw));
        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            receiveData.put(Label.annual_renewable_forecast.name(), Collect.transfer(receiveSprDOs, SprDO::getAnnualRenewableForecast));
            receiveData.put(Label.annual_load_forecast.name(), Collect.transfer(receiveSprDOs, SprDO::getAnnualLoadForecast));
        } else if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL || marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            receiveData.put(Label.annual_renewable_forecast.name(), Collect.transfer(receiveSprDOs, SprDO::getMonthlyRenewableForecast));
            receiveData.put(Label.annual_load_forecast.name(), Collect.transfer(receiveSprDOs, SprDO::getMonthlyLoadForecast));
        } else {
            receiveData.put(Label.annual_renewable_forecast.name(), Collect.transfer(receiveSprDOs, SprDO::getDaRenewableForecast));
            receiveData.put(Label.annual_load_forecast.name(), Collect.transfer(receiveSprDOs, SprDO::getDaLoadForecast));
        }

        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TpbfsdDO::getRoundId, Round.ONE.getDbCode());
        queryWrapper.eq(TpbfsdDO::getProv, Province.RECEIVER.getDbCode());

        transferTpbfsdDOS = tpbfsdMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(TpbfsdDO::getPrd))
                .collect(Collectors.toList());

        transferData.put(Label.annual_receive_forecast_mw.name(), Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getAnnualReceivingForecastMw));

        result.put(Label.receiver_96_analysis.name(), receiveData);

        Map<String, List<Double>> linkData = new HashMap<>();
        linkData.put(Label.receive_target_lower_limit.name(), Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMaxAnnualReceivingMw));

        linkData.put(Label.receive_target_upper_limit.name(), Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMinAnnualReceivingMw));

        result.put(Label.inter_provincial_linking.name(), linkData);

        return result;
    }


    final BlockThermalUnitMapper blockThermalUnitMapper;
    final SubregionBasicMapper subregionBasicMapper;
    final ThermalUnitCostMapper thermalUnitCostMapper;
    final SubregionParameterMapper subregionParameterMapper;
    @GetMapping("listBlockThermalUnit")
    public Map<String, Block> listBlockThermalUnit(String marketTypeValue) {
        MarketType marketType = MarketType.valueOf(marketTypeValue);
        List<SubregionBasicDO> subregionBasicDOs = subregionBasicMapper.selectList(null);
        Map<String, Block> blockMap = new HashMap<>();
        for (SubregionBasicDO subregionBasicDO : subregionBasicDOs) {
            String name = Kit.enumOfMightEx(Province::getDbCode, subregionBasicDO.getProv()).getDesc() + subregionBasicDO.getSubregionName();

            LambdaQueryWrapper<ThermalUnitCostDO> queryWrapper0 = new LambdaQueryWrapper<>();
            queryWrapper0.eq(ThermalUnitCostDO::getSubregionId, subregionBasicDO.getSubregionId());
            List<ThermalUnitCostDO> thermalUnitCostDOs = thermalUnitCostMapper.selectList(queryWrapper0)
                    .stream().sorted(Comparator.comparing(ThermalUnitCostDO::getPrd)).collect(Collectors.toList());

            List<Triple<Double, Double, Double>> histograms = thermalUnitCostDOs.stream()
                    .map(tDO -> Triple.of(tDO.getCostLeftInterval(), tDO.getCostRightInterval(), tDO.getThermalMw()))
                    .collect(Collectors.toList());
            LambdaQueryWrapper<SubregionParameterDO> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(SubregionParameterDO::getSubregionId, subregionBasicDO.getSubregionId());
            List<SubregionParameterDO> subregionParameterDOs = subregionParameterMapper.selectList(queryWrapper1);

            List<Double> loadForecast;
            List<Double> renewableForecast;
            if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
                loadForecast = subregionParameterDOs.stream().map(SubregionParameterDO::getAnnualLoadForecast).collect(Collectors.toList());
                renewableForecast = subregionParameterDOs.stream().map(SubregionParameterDO::getAnnualRenewableForecast).collect(Collectors.toList());
            } else if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL || marketType == MarketType.INTRA_MONTHLY_PROVINCIAL) {
                loadForecast = subregionParameterDOs.stream().map(SubregionParameterDO::getAnnualLoadForecast).collect(Collectors.toList());
                renewableForecast = subregionParameterDOs.stream().map(SubregionParameterDO::getAnnualRenewableForecast).collect(Collectors.toList());
            } else if (marketType == MarketType.INTRA_SPOT_PROVINCIAL || marketType == MarketType.INTER_SPOT_PROVINCIAL){
                loadForecast = subregionParameterDOs.stream().map(SubregionParameterDO::getAnnualLoadForecast).collect(Collectors.toList());
                renewableForecast = subregionParameterDOs.stream().map(SubregionParameterDO::getAnnualRenewableForecast).collect(Collectors.toList());
            } else {
                throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
            }

            Map<String, List<Double>> lineChart = new HashMap<>();
            lineChart.put(Label.blockLoadForecast.name(), loadForecast);
            lineChart.put(Label.blockRenewableForecast.name(), renewableForecast);

            blockMap.put(name, Block.builder().histograms(histograms).lineChart(lineChart).build());
        }
        return blockMap;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class Block {
        List<Triple<Double, Double, Double>> histograms;
        Map<String, List<Double>> lineChart;
    }


    final GeneratorOutputStateMapper generatorOutputStateMapper;
    final GeneratorDOMapper generatorDOMapper;
    final LoadDOMapper loadDOMapper;
    final DomainTunnel domainTunnel;
    final RenewableUnitDOMapper renewableUnitDOMapper;
    final UnitDOMapper unitDOMapper;
    @GetMapping("listAgentInventory")
    public Map<String, Map<String, List<Double>>> listAgentInventory(@RequestParam @NotBlank String marketTypeValue,
                                                                     @RequestParam @NotNull Long compId,
                                                                     @RequestHeader @NotBlank String token) {
        MarketType marketType = MarketType.valueOf(marketTypeValue);
        Integer userId = Integer.parseInt(TokenUtils.getUserId(token));

        LambdaQueryWrapper<UnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UnitDO::getCompId, compId);
        queryWrapper.eq(UnitDO::getUserId, userId);


        List<UnitDO> unitDOS = unitDOMapper.selectList(queryWrapper);
        SysEx.trueThrow(unitDOS.size() == 4, ErrorEnums.SYS_EX);

        Map<String, Map<String, List<Double>>> result = new HashMap<>();
        Pair<String, Map<String, List<Double>>> mapPair;

        List<UnitDO> generatorUnitDOs = unitDOS.stream()
                .filter(unitDO -> Kit.eq(unitDO.getUnitType(), UnitType.GENERATOR.name())).collect(Collectors.toList());
        List<Unit> units = Collect.transfer(unitDOS, UnitDAOAdapter.Convertor.INST::to);
        List<List<AbstractMetaUnit>> metaUnits = units.stream().map(Unit::getMetaUnit).collect(Collect.select(
                u -> Kit.eq(u.getUnitType(), UnitType.GENERATOR),
                u -> Kit.eq(u.getUnitType(), UnitType.LOAD)
        ));
        mapPair = loadGenerator(metaUnits.get(0).get(0).getSourceId(), marketType);
        result.put(mapPair.getKey(), mapPair.getValue());

        mapPair = loadGenerator(metaUnits.get(0).get(1).getSourceId(), marketType);
        result.put(mapPair.getKey(), mapPair.getValue());

        mapPair = loadLoad(metaUnits.get(1).get(0).getSourceId());
        result.put(mapPair.getKey(), mapPair.getValue());


        mapPair = loadLoad(metaUnits.get(1).get(1).getSourceId());
        result.put(mapPair.getKey(), mapPair.getValue());

        return result;
    }




    private Pair<String, Map<String, List<Double>>> loadGenerator(Integer generatorId, MarketType marketType) {
        GeneratorDO generatorDO = generatorDOMapper.selectById(generatorId);
        Map<String, List<Double>> map = new HashMap<>();
        List<Double> maxPs = new ArrayList<>();
        IntStream.range(0, 16).forEach(i -> maxPs.add(generatorDO.getMaxP()));
        if (generatorDO.getType() == 1) {
            map.put(Label.maxPs.name(), maxPs);
        } else {
            LambdaQueryWrapper<RenewableUnitDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RenewableUnitDO::getUnitId, generatorId);
            List<Double> generatorForecast;
            if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
                generatorForecast = renewableUnitDOMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(RenewableUnitDO::getPrd))
                        .map(RenewableUnitDO::getAnnualForecast).collect(Collectors.toList());
            } else if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL || marketType == MarketType.INTRA_MONTHLY_PROVINCIAL) {
                generatorForecast = renewableUnitDOMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(RenewableUnitDO::getPrd))
                        .map(RenewableUnitDO::getMonthlyForecast).collect(Collectors.toList());
            } else if (marketType == MarketType.INTRA_SPOT_PROVINCIAL || marketType == MarketType.INTER_SPOT_PROVINCIAL) {
                generatorForecast = renewableUnitDOMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(RenewableUnitDO::getPrd))
                        .map(RenewableUnitDO::getDaForecast).collect(Collectors.toList());
            } else {
                throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
            }
            map.put(Label.generatorForecast.name(), generatorForecast);
        }
        LambdaQueryWrapper<GeneratorOutputStateDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GeneratorOutputStateDO::getUnitId, generatorId);
        List<Double> baseContractMws = generatorOutputStateMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(GeneratorOutputStateDO::getPrd))
                .map(GeneratorOutputStateDO::getBaseMw).collect(Collectors.toList());
        map.put(Label.baseContractMws.name(), baseContractMws);
        return Pair.of(generatorDO.getUnitName(), map);
    }

    final LoadForecastMapper loadForecastMapper;
    private Pair<String, Map<String, List<Double>>> loadLoad(Integer loadId) {
        LoadDO loadDO = loadDOMapper.selectById(loadId);
        Map<String, List<Double>> map = new HashMap<>();
        LambdaQueryWrapper<LoadForecastDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LoadForecastDO::getLoadId, loadId);
        List<Double> baseMws = loadForecastMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(LoadForecastDO::getPrd))
                .map(LoadForecastDO::getAnnualForecast).collect(Collectors.toList());
        map.put(Label.loadForecast.name(), baseMws);
        return Pair.of(loadDO.getLoadName(), map);
    }


}
