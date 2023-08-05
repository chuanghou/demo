package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.Round;
import com.stellariver.milky.demo.common.enums.TimeFrame;
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

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@RestController
@RequiredArgsConstructor
@RequestMapping("dataController")
public class DataController {

    final SprMapper sprMapper;
    final TpbfsdMapper tpbfsdMapper;
    final MarketSettingMapper marketSettingMapper;

    /**
     * 市场公告
     */
    @GetMapping("marketAnnouncement")
    public Map<Label, String> marketAnnouncement() {
        MarketSettingDO marketSettingDO = marketSettingMapper.selectById(1L);
        Map<Label, String> result = new HashMap<>();
        result.put(Label.offer_price_cap, String.format("%.2f", marketSettingDO.getOffer_price_cap()));
        result.put(Label.offer_price_floor, String.format("%.2f", marketSettingDO.getOffer_price_floor()));
        result.put(Label.bid_price_cap, String.format("%.2f", marketSettingDO.getBid_price_cap()));
        result.put(Label.bid_price_floor, String.format("%.2f", marketSettingDO.getBid_price_cap()));

        result.put(Label.load_annual_max_forecast_err, String.format("%.2f", marketSettingDO.getLoad_annual_max_forecast_err()));
        result.put(Label.load_monthly_max_forecast_err, String.format("%.2f", marketSettingDO.getLoad_monthly_max_forecast_err()));
        result.put(Label.load_da_max_forecast_err, String.format("%.2f", marketSettingDO.getLoad_da_max_forecast_err()));
        result.put(Label.renewable_annual_max_forecast_err, String.format("%.2f", marketSettingDO.getRenewable_annual_max_forecast_err()));
        result.put(Label.renewable_monthly_max_forecast_err, String.format("%.2f", marketSettingDO.getRenewable_monthly_max_forecast_err()));
        result.put(Label.renewable_da_max_forecast_err, String.format("%.2f", marketSettingDO.getRenewable_da_max_forecast_err()));

        result.put(Label.transmission_and_distribution_tariff, String.format("%.2f", marketSettingDO.getTransmission_and_distribution_tariff()));
        result.put(Label.regulated_user_tariff, String.format("%.2f", marketSettingDO.getRegulated_user_tariff()));
        result.put(Label.regulated_producer_price, String.format("%.2f", marketSettingDO.getRegulated_producer_price()));
        result.put(Label.regulated_interprov_transmission_price, String.format("%.2f", marketSettingDO.getRegulated_interprov_transmission_price()));

        result.put(Label.round_id, String.valueOf(marketSettingDO.getRound_id()));

        result.put(Label.sender_peak_prds, TimeFrame.PEAK.getPrds().stream().map(Object::toString).collect(Collectors.joining(", ")));
        result.put(Label.sender_flat_prds, TimeFrame.FLAT.getPrds().stream().map(Object::toString).collect(Collectors.joining(", ")));
        result.put(Label.sender_valley_prds, TimeFrame.VALLEY.getPrds().stream().map(Object::toString).collect(Collectors.joining(", ")));

        result.put(Label.receive_peak_prds, TimeFrame.PEAK.getPrds().stream().map(Object::toString).collect(Collectors.joining(", ")));
        result.put(Label.receive_flat_prds, TimeFrame.FLAT.getPrds().stream().map(Object::toString).collect(Collectors.joining(", ")));
        result.put(Label.receive_valley_prds,TimeFrame.VALLEY.getPrds().stream().map(Object::toString).collect(Collectors.joining(", ")));

        return result;
    }

    /**
     * 代理概况
     */
    @GetMapping("agentData")
    public Map<Label, String> agentData(String unitType, @Nullable String generatorType, Integer sourceId) {
        Map<Label, String> result = new HashMap<>();
        if (UnitType.valueOf(unitType) == UnitType.GENERATOR) {
            GeneratorDO generatorDO = generatorDOMapper.selectById(sourceId);
            if (GeneratorType.valueOf(generatorType) == GeneratorType.CLASSIC) {
                result.put(Label.unit_name, generatorDO.getUnit_name());
                result.put(Label.prov_of_generator, Kit.enumOfMightEx(Province::getDbCode, generatorDO.getProv()).getDesc());
                result.put(Label.node_id_of_generator, String.valueOf(generatorDO.getNode_id()));
                result.put(Label.min_off_duration, String.format("%.2f",generatorDO.getMin_off_duration()));
                result.put(Label.min_on_duration, String.format("%.2f",generatorDO.getMin_on_duration()));
                result.put(Label.ramp_up_rate, String.format("%.2f",generatorDO.getRamp_up_rate()));
                result.put(Label.ramp_dn_rate, String.format("%.2f",generatorDO.getMin_on_duration()));
                result.put(Label.max_p_of_classic_generator, String.format("%.2f",generatorDO.getMax_p()));
                result.put(Label.min_p_of_classic_generator, String.format("%.2f",generatorDO.getMin_p()));
                String startup_curve_x = Stream.of(generatorDO.getStartup_curve_1(),
                        generatorDO.getStartup_curve_2(),
                        generatorDO.getStartup_curve_3(),
                        generatorDO.getStartup_curve_4(),
                        generatorDO.getStartup_curve_5(),
                        generatorDO.getStartup_curve_6()).map(Object::toString).collect(Collectors.joining(", "));
                result.put(Label.startup_curve_x, startup_curve_x);

                String shutdown_curve_x = Stream.of(generatorDO.getShutdown_curve_1(),
                        generatorDO.getShutdown_curve_2(),
                        generatorDO.getShutdown_curve_3(),
                        generatorDO.getShutdown_curve_4(),
                        generatorDO.getShutdown_curve_5(),
                        generatorDO.getShutdown_curve_6()).map(Object::toString).collect(Collectors.joining(", "));
                result.put(Label.shutdown_curve_x, shutdown_curve_x);

                result.put(Label.num_startup_curve_prds, String.valueOf(generatorDO.getNum_startup_curve_prds()));
                result.put(Label.num_shutdown_curve_prds, String.valueOf(generatorDO.getNum_shutdown_curve_prds()));
            } else {
                result.put(Label.unit_name, generatorDO.getUnit_name());
                result.put(Label.prov_of_generator, Kit.enumOfMightEx(Province::getDbCode, generatorDO.getProv()).getDesc());
                result.put(Label.node_id_of_generator, String.valueOf(generatorDO.getNode_id()));
                result.put(Label.max_p_of_renewable, String.format("%.2f",generatorDO.getMax_p()));
            }
        } else {
            LoadDO loadDO = loadDOMapper.selectById(sourceId);
            result.put(Label.load_name, loadDO.getLoad_name());
            result.put(Label.prov_of_load, Kit.enumOfMightEx(Province::getDbCode, loadDO.getPrv()).getDesc());
            result.put(Label.node_id_of_load, String.valueOf(loadDO.getNode_id()));
            result.put(Label.max_p_of_load,  String.format("%.2f",loadDO.getMax_p()));

        }
        return result;
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

        result.put(Label.transfer_96_analysis.name(), transferData);


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
            queryWrapper0.last("limit 10");
            List<ThermalUnitCostDO> thermalUnitCostDOs = thermalUnitCostMapper.selectList(queryWrapper0)
                    .stream().sorted(Comparator.comparing(ThermalUnitCostDO::getPrd)).collect(Collectors.toList());

            List<Triple<Double, Double, Double>> histograms = thermalUnitCostDOs.stream()
                    .map(tDO -> Triple.of(tDO.getCostLeftInterval(), tDO.getThermalMw(), tDO.getCostRightInterval()))
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

        List<Unit> units = Collect.transfer(unitDOS, UnitDAOAdapter.Convertor.INST::to);
        List<List<AbstractMetaUnit>> metaUnits = units.stream().map(Unit::getMetaUnit).collect(Collect.select(
                u -> Kit.eq(u.getUnitType(), UnitType.GENERATOR),
                u -> Kit.eq(u.getUnitType(), UnitType.LOAD)
        ));

        Integer sourceId = metaUnits.get(0).get(0).getSourceId();
        mapPair = loadGenerator(sourceId, marketType);
        result.put(mapPair.getKey(), mapPair.getValue());

        sourceId = metaUnits.get(0).get(1).getSourceId();

        mapPair = loadGenerator(sourceId, marketType);
        result.put(mapPair.getKey(), mapPair.getValue());

        sourceId = metaUnits.get(1).get(0).getSourceId();
        mapPair = loadLoad(sourceId);
        result.put(mapPair.getKey(), mapPair.getValue());

        sourceId = metaUnits.get(1).get(1).getSourceId();
        mapPair = loadLoad(sourceId);
        result.put(mapPair.getKey(), mapPair.getValue());

        return result;
    }




    private Pair<String, Map<String, List<Double>>> loadGenerator(Integer generatorId, MarketType marketType) {
        GeneratorDO generatorDO = generatorDOMapper.selectById(generatorId);
        Map<String, List<Double>> map = new HashMap<>();
        List<Double> maxPs = new ArrayList<>();
        IntStream.range(0, 96).forEach(i -> maxPs.add(generatorDO.getMax_p()));
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
        return Pair.of(generatorDO.getUnit_name(), map);
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
        return Pair.of(loadDO.getLoad_name(), map);
    }


}
