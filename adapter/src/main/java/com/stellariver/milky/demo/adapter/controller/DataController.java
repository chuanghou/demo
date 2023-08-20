package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.Round;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.common.enums.UnitType;
import com.stellariver.milky.demo.domain.*;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.stellariver.milky.demo.basic.Label.generatorDiffDeals;


@RestController
@RequiredArgsConstructor
@RequestMapping("dataController")
public class DataController {

    final SprMapper sprMapper;
    final TpbfsdMapper tpbfsdMapper;
    final MarketSettingMapper marketSettingMapper;
    final MetaUnitDOMapper metaUnitDOMapper;

    /**
     * 市场公告
     */
    @GetMapping("marketAnnouncement")
    public Map<String, Map<Label, String>> marketAnnouncement() {
        MarketSettingDO marketSettingDO = marketSettingMapper.selectById(1L);
        Map<String, Map<Label, String>> result = new LinkedHashMap<>();

        Map<Label, String> result1 = new LinkedHashMap<>();
        result1.put(Label.offer_price_cap, String.format("%.2f", marketSettingDO.getOfferPriceCap()));
        result1.put(Label.offer_price_floor, String.format("%.2f", marketSettingDO.getOfferPriceFloor()));
        result1.put(Label.bid_price_cap, String.format("%.2f", marketSettingDO.getBidPriceCap()));
        result1.put(Label.bid_price_floor, String.format("%.2f", marketSettingDO.getBidPriceCap()));
        result.put("全市场报价上下限", result1);

        Map<Label, String> result2 = new LinkedHashMap<>();
        result2.put(Label.load_annual_max_forecast_err, String.format("%.2f", marketSettingDO.getLoadAnnualMaxForecastErr()));
        result2.put(Label.load_monthly_max_forecast_err, String.format("%.2f", marketSettingDO.getLoadMonthlyMaxForecastErr()));
        result2.put(Label.load_da_max_forecast_err, String.format("%.2f", marketSettingDO.getLoadDaMaxForecastErr()));
        result2.put(Label.renewable_annual_max_forecast_err, String.format("%.2f", marketSettingDO.getRenewableAnnualMaxForecastErr()));
        result2.put(Label.renewable_monthly_max_forecast_err, String.format("%.2f", marketSettingDO.getRenewableMonthlyMaxForecastErr()));
        result2.put(Label.renewable_da_max_forecast_err, String.format("%.2f", marketSettingDO.getRenewableDaMaxForecastErr()));
        result.put("负荷与新能源发电预测偏差", result2);

        Map<Label, String> result3 = new LinkedHashMap<>();
        result3.put(Label.transmission_and_distribution_tariff, String.format("%.2f", marketSettingDO.getTransmissionAndDistributionTariff()));
        result3.put(Label.regulated_user_tariff, String.format("%.2f", marketSettingDO.getRegulatedUserTariff()));
        result3.put(Label.regulated_producer_price, String.format("%.2f", marketSettingDO.getRegulatedProducerPrice()));
        result3.put(Label.regulated_interprov_transmission_price, String.format("%.2f", marketSettingDO.getRegulatedInterprovTransmissionPrice()));
        result.put("各类非市场化定价", result3);

        Map<Label, String> result4 = new LinkedHashMap<>();
        result4.put(Label.round_id, String.valueOf(marketSettingDO.getRoundId()));
        result4.put(Label.interprov_clearing_mode, marketSettingDO.getInterprov_clearing_mode());
        result4.put(Label.interprov_trading_mode, marketSettingDO.getInterprov_trading_mode());
        result.put("市场状态定义", result4);

        Map<Label, String> result5 = new LinkedHashMap<>();
        result5.put(Label.sender_peak_prds, "06:00-09:00; 15:00-23:00");
        result5.put(Label.sender_flat_prds, "00:00-05:00");
        result5.put(Label.sender_valley_prds, "10:00-14:00");
        result.put("峰平谷定义", result5);


        return result;
    }

    /**
     * 代理概况
     */
    @GetMapping("agentData")
    public Map<Label, String> agentData(String unitType, @Nullable String generatorType, Integer sourceId) {
        Map<Label, String> result = new LinkedHashMap<>();
        if (UnitType.valueOf(unitType) == UnitType.GENERATOR) {
            GeneratorDO generatorDO = generatorDOMapper.selectById(sourceId);
            if (GeneratorType.valueOf(generatorType) == GeneratorType.CLASSIC) {
                result.put(Label.unit_name, generatorDO.getUnitName());
                result.put(Label.prov_of_generator, Kit.enumOfMightEx(Province::getDbCode, generatorDO.getProv()).getDesc());
                result.put(Label.node_id_of_generator, String.valueOf(generatorDO.getNodeId()));
                result.put(Label.min_off_duration, String.format("%.2f",generatorDO.getMinOffDuration()));
                result.put(Label.min_on_duration, String.format("%.2f",generatorDO.getMinOnDuration()));
                result.put(Label.ramp_up_rate, String.format("%.2f",generatorDO.getRampUpRate()));
                result.put(Label.ramp_dn_rate, String.format("%.2f",generatorDO.getMinOnDuration()));
                result.put(Label.max_p_of_classic_generator, String.format("%.2f",generatorDO.getMaxP()));
                result.put(Label.min_p_of_classic_generator, String.format("%.2f",generatorDO.getMinP()));
                String startup_curve_x = Stream.of(generatorDO.getStartupCurve1(),
                        generatorDO.getStartupCurve2(),
                        generatorDO.getStartupCurve3(),
                        generatorDO.getStartupCurve4(),
                        generatorDO.getStartupCurve5(),
                        generatorDO.getStartupCurve6()).map(Object::toString).collect(Collectors.joining(", "));
                result.put(Label.startup_curve_x, startup_curve_x);

                String shutdown_curve_x = Stream.of(generatorDO.getShutdownCurve1(),
                        generatorDO.getShutdownCurve2(),
                        generatorDO.getShutdownCurve3(),
                        generatorDO.getShutdownCurve4(),
                        generatorDO.getShutdownCurve5(),
                        generatorDO.getShutdownCurve6()).map(Object::toString).collect(Collectors.joining(", "));
                result.put(Label.shutdown_curve_x, shutdown_curve_x);

                result.put(Label.num_startup_curve_prds, String.valueOf(generatorDO.getNumStartupCurvePrds()));
                result.put(Label.num_shutdown_curve_prds, String.valueOf(generatorDO.getNumShutdownCurvePrds()));
            } else {
                result.put(Label.unit_name, generatorDO.getUnitName());
                result.put(Label.prov_of_generator, Kit.enumOfMightEx(Province::getDbCode, generatorDO.getProv()).getDesc());
                result.put(Label.node_id_of_generator, String.valueOf(generatorDO.getNodeId()));
                result.put(Label.max_p_of_renewable, String.format("%.2f",generatorDO.getMaxP()));
            }
        } else {
            LoadDO loadDO = loadDOMapper.selectById(sourceId);
            result.put(Label.load_name, loadDO.getLoadName());
            result.put(Label.prov_of_load, Kit.enumOfMightEx(Province::getDbCode, loadDO.getProv()).getDesc());
            result.put(Label.node_id_of_load, String.valueOf(loadDO.getNodeId()));
            result.put(Label.max_p_of_load,  String.format("%.2f",loadDO.getMaxP()));

        }
        return result;
    }


    /**
     * 市场概况
     */
    @GetMapping("marketProfile")
    public List<Map<Label, String>> marketData(@RequestParam("marketTypeValue") String marketTypeValue) {

        MarketType marketType = MarketType.valueOf(marketTypeValue);

        LinkedHashMap<Label, String> result0 = new LinkedHashMap<>();
        result0.put(Label.market_profile_locate_province, Province.TRANSFER.getDesc());

        Comp comp = tunnel.runningComp();
        Long compId = comp.getCompId();
        Integer roundId = comp.getRoundId();

        LambdaQueryWrapper<UnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UnitDO::getCompId, comp.getCompId());
        queryWrapper.eq(UnitDO::getRoundId, comp.getRoundId());
        List<Integer> metaUnitIds = Collect.transfer(unitDOMapper.selectList(queryWrapper), UnitDAOAdapter.Convertor.INST::to)
                .stream().map(u -> u.getMetaUnit().getMetaUnitId()).collect(Collectors.toList());

        List<MetaUnitDO> metaUnitDOs = metaUnitDOMapper.selectBatchIds(metaUnitIds);
        String transferInfo = metaUnitDOs.stream().filter(m -> m.getUnitType().equals(UnitType.GENERATOR.name()))
                .filter(m -> m.getProvince().equals(Province.TRANSFER.name()))
                .collect(Collectors.groupingBy(MetaUnitDO::getNameType))
                .entrySet().stream().map(e -> e.getKey() + e.getValue().size() + "台")
                .collect(Collectors.joining("\n"));
        result0.put(Label.market_profile_generator, transferInfo);


        List<SprDO> sprDOs = sprMapper.selectList(null);
        List<SprDO> transferSprDOs = sprDOs.stream().filter(sprDO -> sprDO.getProv().equals(Province.TRANSFER.getDbCode())).collect(Collectors.toList());
        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            Double max = transferSprDOs.stream().max(Comparator.comparing(SprDO::getAnnualLoadForecast)).map(SprDO::getAnnualLoadForecast).orElse(0D);
            Double min = transferSprDOs.stream().min(Comparator.comparing(SprDO::getAnnualLoadForecast)).map(SprDO::getAnnualLoadForecast).orElse(0D);
            String value = String.format("全天最高负荷%sMW\n全天最低负荷%sMW", max, min);
            result0.put(Label.market_profile_load, value);
        } else if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL || marketType == MarketType.INTRA_MONTHLY_PROVINCIAL) {
            Double max = transferSprDOs.stream().max(Comparator.comparing(SprDO::getMonthlyLoadForecast)).map(SprDO::getAnnualLoadForecast).orElse(0D);
            Double min = transferSprDOs.stream().min(Comparator.comparing(SprDO::getMonthlyLoadForecast)).map(SprDO::getAnnualLoadForecast).orElse(0D);
            String value = String.format("全天最高负荷%sMW\n全天最低负荷%sMW", max, min);
            result0.put(Label.market_profile_load, value);
        } else {
            Double max = transferSprDOs.stream().max(Comparator.comparing(SprDO::getDaLoadForecast)).map(SprDO::getAnnualLoadForecast).orElse(0D);
            Double min = transferSprDOs.stream().min(Comparator.comparing(SprDO::getDaLoadForecast)).map(SprDO::getAnnualLoadForecast).orElse(0D);
            String value = String.format("全天最高负荷%sMW\n全天最低负荷%sMW", max, min);
            result0.put(Label.market_profile_load, value);
        }



        LinkedHashMap<Label, String> result1 = new LinkedHashMap<>();
        transferInfo = metaUnitDOs.stream().filter(m -> m.getUnitType().equals(UnitType.GENERATOR.name()))
                .filter(m -> m.getProvince().equals(Province.RECEIVER.name()))
                .collect(Collectors.groupingBy(MetaUnitDO::getNameType))
                .entrySet().stream().map(e -> e.getKey() + e.getValue().size() + "台")
                .collect(Collectors.joining("\n"));
        result1.put(Label.market_profile_locate_province, Province.RECEIVER.getDesc());
        result1.put(Label.market_profile_generator, transferInfo);


        result1.put(Label.market_profile_load, "xxx");
        return Arrays.asList(result0, result1);
    }


    @GetMapping("systemParameterRelease")
    Map<Label, Map<Label, List<Double>>> systemParameterRelease(@RequestParam String marketTypeValue) {

        MarketType marketType = MarketType.valueOf(marketTypeValue);

        List<SprDO> sprDOs = sprMapper.selectList(null);

        Map<Label, Map<Label, List<Double>>> result = new LinkedHashMap<>();

        List<SprDO> transferSprDOs = sprDOs.stream()
                .filter(d -> Kit.eq(d.getProv(), Province.TRANSFER.getDbCode()))
                .sorted(Comparator.comparing(SprDO::getDt))
                .collect(Collectors.toList());

        Map<Label, List<Double>> transferData = new LinkedHashMap<>();

        transferData.put(Label.min_thermal_mw, Collect.transfer(transferSprDOs, SprDO::getMinThermalMw));
        transferData.put(Label.adjustable_thermal_mw, Collect.transfer(transferSprDOs, SprDO::getAdjustableThermalMw));

        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            transferData.put(Label.annual_renewable_forecast, Collect.transfer(transferSprDOs, SprDO::getAnnualRenewableForecast));
            transferData.put(Label.annual_load_forecast, Collect.transfer(transferSprDOs, SprDO::getAnnualLoadForecast));
        } else if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL || marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            transferData.put(Label.annual_renewable_forecast, Collect.transfer(transferSprDOs, SprDO::getDaRenewableForecast));
            transferData.put(Label.annual_load_forecast, Collect.transfer(transferSprDOs, SprDO::getMonthlyLoadForecast));
        } else if (marketType == MarketType.INTRA_SPOT_PROVINCIAL || marketType == MarketType.INTER_SPOT_PROVINCIAL) {
            transferData.put(Label.annual_renewable_forecast, Collect.transfer(transferSprDOs, SprDO::getDaRenewableForecast));
            transferData.put(Label.annual_load_forecast, Collect.transfer(transferSprDOs, SprDO::getDaLoadForecast));
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }

        LambdaQueryWrapper<TpbfsdDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TpbfsdDO::getRoundId, Round.ONE.getDbCode());
        queryWrapper.eq(TpbfsdDO::getProv, Province.TRANSFER.getDbCode());

        List<TpbfsdDO> transferTpbfsdDOS = tpbfsdMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(TpbfsdDO::getPrd)).collect(Collectors.toList());
        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            transferData.put(Label.receive_forecast_mw, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getAnnualReceivingForecastMw));
        } else if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL || marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            transferData.put(Label.receive_forecast_mw, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMonthlyReceivingForecastMw));
        } else {
            transferData.put(Label.receive_forecast_mw, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getDaReceivingForecastMw));
        }

        result.put(Label.transfer_96_analysis, transferData);





        List<SprDO> receiveSprDOs = sprDOs.stream()
                .filter(d -> Kit.eq(d.getProv(), Province.RECEIVER.getDbCode()))
                .sorted(Comparator.comparing(SprDO::getDt))
                .collect(Collectors.toList());

        Map<Label, List<Double>> receiveData = new LinkedHashMap<>();
        receiveData.put(Label.min_thermal_mw, Collect.transfer(receiveSprDOs, SprDO::getMinThermalMw));
        receiveData.put(Label.adjustable_thermal_mw, Collect.transfer(receiveSprDOs, SprDO::getAdjustableThermalMw));
        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            receiveData.put(Label.annual_renewable_forecast, Collect.transfer(receiveSprDOs, SprDO::getAnnualRenewableForecast));
            receiveData.put(Label.annual_load_forecast, Collect.transfer(receiveSprDOs, SprDO::getAnnualLoadForecast));
        } else if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL || marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            receiveData.put(Label.annual_renewable_forecast, Collect.transfer(receiveSprDOs, SprDO::getMonthlyRenewableForecast));
            receiveData.put(Label.annual_load_forecast, Collect.transfer(receiveSprDOs, SprDO::getMonthlyLoadForecast));
        } else {
            receiveData.put(Label.annual_renewable_forecast, Collect.transfer(receiveSprDOs, SprDO::getDaRenewableForecast));
            receiveData.put(Label.annual_load_forecast, Collect.transfer(receiveSprDOs, SprDO::getDaLoadForecast));
        }

        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            receiveData.put(Label.receive_forecast_mw, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getAnnualReceivingForecastMw));
        } else if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL || marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            receiveData.put(Label.receive_forecast_mw, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMonthlyReceivingForecastMw));
        } else {
            receiveData.put(Label.receive_forecast_mw, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getDaReceivingForecastMw));
        }
        result.put(Label.receiver_96_analysis, receiveData);




        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL) {
            Map<Label, List<Double>> linkData = new LinkedHashMap<>();
            linkData.put(Label.receive_target_lower_limit, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMaxAnnualReceivingMw));
            linkData.put(Label.receive_target_upper_limit, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMinAnnualReceivingMw));
            result.put(Label.inter_provincial_linking, linkData);
        } else if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            Map<Label, List<Double>> linkData = new LinkedHashMap<>();
            linkData.put(Label.receive_target_upper_limit, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMaxMonthlyReceivingMw));
            linkData.put(Label.receive_target_lower_limit, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMinMonthlyReceivingMw));
            linkData.put(Label.intraprovincial_monthly_tieline_power, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getIntraprovincialMonthlyTielinePower));
            result.put(Label.inter_provincial_linking, linkData);
        } else if (marketType == MarketType.INTRA_SPOT_PROVINCIAL || marketType == MarketType.INTER_SPOT_PROVINCIAL) {
            Map<Label, List<Double>> linkData = new LinkedHashMap<>();
            linkData.put(Label.da_receiving_target, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getDaReceivingTarget));
            linkData.put(Label.intraprovincial_annual_tieline_power, Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getIntraprovincialAnnualTielinePower));
            result.put(Label.inter_provincial_linking, linkData);
        }
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
        Map<String, Block> blockMap = new LinkedHashMap<>();
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
                loadForecast = subregionParameterDOs.stream().map(SubregionParameterDO::getMonthlyLoadForecast).collect(Collectors.toList());
                renewableForecast = subregionParameterDOs.stream().map(SubregionParameterDO::getMonthlyRenewableForecast).collect(Collectors.toList());
            } else if (marketType == MarketType.INTRA_SPOT_PROVINCIAL || marketType == MarketType.INTER_SPOT_PROVINCIAL){
                loadForecast = subregionParameterDOs.stream().map(SubregionParameterDO::getDaLoadForecast).collect(Collectors.toList());
                renewableForecast = subregionParameterDOs.stream().map(SubregionParameterDO::getDaRenewableForecast).collect(Collectors.toList());
            } else {
                throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
            }

            Map<String, List<Double>> lineChart = new LinkedHashMap<>();
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
    final Tunnel tunnel;
    @GetMapping("listAgentInventory")
    public Map<String, Map<Label, List<Double>>> listAgentInventory(@RequestParam @NotBlank String marketTypeValue,
                                                                     @RequestHeader @NotBlank String token) {
        MarketType marketType = MarketType.valueOf(marketTypeValue);
        Integer userId = Integer.parseInt(TokenUtils.getUserId(token));

        LambdaQueryWrapper<UnitDO> queryWrapper = new LambdaQueryWrapper<>();
        Comp comp = tunnel.runningComp();
        queryWrapper.eq(UnitDO::getCompId, comp.getCompId());
        queryWrapper.eq(UnitDO::getRoundId, comp.getRoundId());
        queryWrapper.eq(UnitDO::getUserId, userId);


        List<UnitDO> unitDOS = unitDOMapper.selectList(queryWrapper);
        SysEx.falseThrow(unitDOS.size() == 4, ErrorEnums.SYS_EX);

        Map<String, Map<Label, List<Double>>> result = new LinkedHashMap<>();
        Pair<String, Map<Label, List<Double>>> mapPair;

        List<Unit> units = Collect.transfer(unitDOS, UnitDAOAdapter.Convertor.INST::to);
        List<List<Unit>> groupUnits = units.stream().collect(Collect.select(
                u -> Kit.eq(u.getMetaUnit().getUnitType(), UnitType.GENERATOR),
                u -> Kit.eq(u.getMetaUnit().getUnitType(), UnitType.LOAD)
        ));

        groupUnits.get(0).stream().map(u -> loadGenerator(u, marketType)).forEach(p -> {
            result.put(p.getKey(), p.getValue());
        });
        groupUnits.get(1).stream().map(u -> loadLoad(u, marketType)).forEach(p -> {
            result.put(p.getKey(), p.getValue());
        });

        return result;
    }




    private Pair<String, Map<Label, List<Double>>> loadGenerator(Unit unit, MarketType marketType) {
        Map<Label, List<Double>> map = new LinkedHashMap<>();
        Integer generatorId = unit.getMetaUnit().getSourceId();
        GeneratorDO generatorDO = generatorDOMapper.selectById(generatorId);
        GeneratorType generatorType = Kit.enumOfMightEx(GeneratorType::getDbCode, generatorDO.getType());

        if (generatorType == GeneratorType.CLASSIC) {
            List<Double> maxPs = new ArrayList<>();
            IntStream.range(0, 24).forEach(i -> maxPs.add(generatorDO.getMaxP()));
            map.put(Label.maxPs, maxPs);
            map.put(Label.generatorInventoryUpperLimit, maxPs);
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
            map.put(Label.generatorForecast, generatorForecast);
            map.put(Label.generatorInventoryUpperLimit, generatorForecast);
        }

        LambdaQueryWrapper<GeneratorOutputStateDO> queryWrapper0 = new LambdaQueryWrapper<>();
        queryWrapper0.eq(GeneratorOutputStateDO::getUnitId, generatorId);
        List<Double> baseContractMws = generatorOutputStateMapper.selectList(queryWrapper0).stream()
                .sorted(Comparator.comparing(GeneratorOutputStateDO::getPrd))
                .map(GeneratorOutputStateDO::getBaseMw).collect(Collectors.toList());
        map.put(Label.baseContractMws, baseContractMws);
        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL) {
            map.put(generatorDiffDeals, baseContractMws);
        } else if (marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            List<Double> deals0 = resolveDeals(unit, MarketType.INTER_ANNUAL_PROVINCIAL);
            List<Double> collect = IntStream.range(0, 24).mapToDouble(i -> deals0.get(i) + baseContractMws.get(i))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            map.put(Label.generatorDiffDeals, collect);
            map.put(Label.generatorInterProvinceAnnualDeal, deals0);
        } else if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            List<Double> deals0 = resolveDeals(unit, MarketType.INTER_ANNUAL_PROVINCIAL);
            List<Double> deals1 = resolveDeals(unit, MarketType.INTRA_ANNUAL_PROVINCIAL);
            List<Double> collect = IntStream.range(0, 24).mapToDouble(i -> deals0.get(i) + deals1.get(i) + baseContractMws.get(i))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            map.put(Label.generatorDiffDeals, collect);
            map.put(Label.generatorInterProvinceAnnualDeal, deals0);
            map.put(Label.generatorIntraProvinceAnnualDeal, deals1);
        } else if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL) {
            List<Double> deals0 = resolveDeals(unit, MarketType.INTER_ANNUAL_PROVINCIAL);
            List<Double> deals1 = resolveDeals(unit, MarketType.INTRA_ANNUAL_PROVINCIAL);
            List<Double> deals2 = resolveDeals(unit, MarketType.INTER_MONTHLY_PROVINCIAL);
            List<Double> collect = IntStream.range(0, 24).mapToDouble(i -> deals0.get(i) + deals1.get(i) + deals2.get(i) + baseContractMws.get(i))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            map.put(Label.generatorDiffDeals, collect);
            map.put(Label.generatorInterProvinceAnnualDeal, deals0);
            map.put(Label.generatorIntraProvinceAnnualDeal, deals1);
            map.put(Label.generatorInterProvinceMonthlyDeal, deals2);
        } else if (marketType == MarketType.INTRA_SPOT_PROVINCIAL || marketType == MarketType.INTER_SPOT_PROVINCIAL) {
            List<Double> deals0 = resolveDeals(unit, MarketType.INTER_ANNUAL_PROVINCIAL);
            List<Double> deals1 = resolveDeals(unit, MarketType.INTRA_ANNUAL_PROVINCIAL);
            List<Double> deals2 = resolveDeals(unit, MarketType.INTER_MONTHLY_PROVINCIAL);
            List<Double> deals3 = resolveDeals(unit, MarketType.INTRA_MONTHLY_PROVINCIAL);
            List<Double> collect = IntStream.range(0, 24).mapToDouble(i -> deals0.get(i) + deals1.get(i) + deals2.get(i) + deals3.get(i) + baseContractMws.get(i))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            map.put(Label.generatorDiffDeals, collect);
            map.put(Label.generatorInterProvinceAnnualDeal, deals0);
            map.put(Label.generatorIntraProvinceAnnualDeal, deals1);
            map.put(Label.generatorInterProvinceMonthlyDeal, deals2);
            map.put(Label.generatorIntraProvinceMonthlyDeal, deals3);
        }

        return Pair.of(generatorDO.getUnitName(), map);
    }

    List<Double> resolveDeals(Unit unit, MarketType marketType) {
        List<Double> deals = new ArrayList<>(24);
        unit.getBids().values().stream()
                .filter(bid -> bid.getMarketType() ==marketType)
                .collect(Collectors.groupingBy(Bid::getTimeFrame))
                .forEach(((t, bids) -> {
                    Double aDouble = bidDealTotal(bids);
                    t.getPrds().forEach(prd -> deals.set(prd, aDouble));
                }));
        return deals;
    }

    Double bidDealTotal(List<Bid> bids) {
        return bids.stream().flatMap(bid -> bid.getDeals().stream()).map(Deal::getQuantity).reduce(0D, Double::sum);
    }


    final LoadForecastMapper loadForecastMapper;
    private Pair<String, Map<Label, List<Double>>> loadLoad(Unit unit, MarketType marketType) {
        Integer sourceId = unit.getMetaUnit().getSourceId();
        LoadDO loadDO = loadDOMapper.selectById(sourceId);
        Map<Label, List<Double>> map = new LinkedHashMap<>();
        LambdaQueryWrapper<LoadForecastDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LoadForecastDO::getLoadId, sourceId);
        List<LoadForecastDO> loadForecastDOs = loadForecastMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(LoadForecastDO::getPrd)).collect(Collectors.toList());
        if(marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            List<Double> collect = loadForecastDOs.stream().map(LoadForecastDO::getAnnualForecast).collect(Collectors.toList());
            map.put(Label.loadForecast, collect);
            map.put(Label.loadInventoryUpperLimit, collect);
        } else if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL || marketType == MarketType.INTRA_MONTHLY_PROVINCIAL) {
            List<Double> collect = loadForecastDOs.stream().map(LoadForecastDO::getMonthlyForecast).collect(Collectors.toList());
            map.put(Label.loadForecast, collect);
            map.put(Label.loadInventoryUpperLimit, collect);
        } else {
            List<Double> collect = loadForecastDOs.stream().map(LoadForecastDO::getDaForecast).collect(Collectors.toList());
            map.put(Label.loadForecast, collect);
            map.put(Label.loadInventoryUpperLimit, collect);
        }

        List<Double> loadDiffDeals = IntStream.range(0, 24).mapToDouble(i -> 0D)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL) {
            map.put(Label.loadDiffDeals, loadDiffDeals);
        } else if (marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            List<Double> deals0 = resolveDeals(unit, MarketType.INTER_ANNUAL_PROVINCIAL);
            List<Double> collect = IntStream.range(0, 24).mapToDouble(i -> deals0.get(i) + loadDiffDeals.get(i))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            map.put(Label.loadDiffDeals, collect);
            map.put(Label.loadInterProvinceAnnualDeal, deals0);
        } else if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            List<Double> deals0 = resolveDeals(unit, MarketType.INTER_ANNUAL_PROVINCIAL);
            List<Double> deals1 = resolveDeals(unit, MarketType.INTRA_ANNUAL_PROVINCIAL);
            List<Double> collect = IntStream.range(0, 24).mapToDouble(i -> deals0.get(i) + deals1.get(i) + loadDiffDeals.get(i))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            map.put(Label.loadDiffDeals, collect);
            map.put(Label.loadInterProvinceAnnualDeal, deals0);
            map.put(Label.loadIntraProvinceAnnualDeal, deals1);
        } else if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL) {
            List<Double> deals0 = resolveDeals(unit, MarketType.INTER_ANNUAL_PROVINCIAL);
            List<Double> deals1 = resolveDeals(unit, MarketType.INTRA_ANNUAL_PROVINCIAL);
            List<Double> deals2 = resolveDeals(unit, MarketType.INTER_MONTHLY_PROVINCIAL);
            List<Double> collect = IntStream.range(0, 24).mapToDouble(i -> deals0.get(i) + deals1.get(i) + deals2.get(i) + loadDiffDeals.get(i))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            map.put(Label.loadDiffDeals, collect);
            map.put(Label.loadInterProvinceAnnualDeal, deals0);
            map.put(Label.loadIntraProvinceAnnualDeal, deals1);
            map.put(Label.loadInterProvinceMonthlyDeal, deals2);
        } else if (marketType == MarketType.INTRA_SPOT_PROVINCIAL || marketType == MarketType.INTER_SPOT_PROVINCIAL) {
            List<Double> deals0 = resolveDeals(unit, MarketType.INTER_ANNUAL_PROVINCIAL);
            List<Double> deals1 = resolveDeals(unit, MarketType.INTRA_ANNUAL_PROVINCIAL);
            List<Double> deals2 = resolveDeals(unit, MarketType.INTER_MONTHLY_PROVINCIAL);
            List<Double> deals3 = resolveDeals(unit, MarketType.INTRA_MONTHLY_PROVINCIAL);
            List<Double> collect = IntStream.range(0, 24).mapToDouble(i -> deals0.get(i) + deals1.get(i) + deals2.get(i) + deals3.get(i) + loadDiffDeals.get(i))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            map.put(Label.loadDiffDeals, collect);
            map.put(Label.loadInterProvinceAnnualDeal, deals0);
            map.put(Label.loadIntraProvinceAnnualDeal, deals1);
            map.put(Label.loadInterProvinceMonthlyDeal, deals2);
            map.put(Label.loadIntraProvinceMonthlyDeal, deals3);
        }

        return Pair.of(loadDO.getLoadName(), map);
    }

    final StartupShutdownCostDOMapper startupShutdownCostDOMapper;
    final ThermalUnitOperatingCostMapper thermalUnitOperatingCostMapper;

    @GetMapping("costOfClassicOfAnnualAndMonthly")
    public Map<Label, String> costOfClassicOfAnnualAndMonthly(Long unitId) {
        Unit unit = domainTunnel.getByAggregateId(Unit.class, String.valueOf(unitId));
        Integer sourceId = unit.getMetaUnit().getSourceId();
        Map<Label, String> result = new LinkedHashMap<>();
        LambdaQueryWrapper<StartupShutdownCostDO> eq = new LambdaQueryWrapper<StartupShutdownCostDO>().eq(StartupShutdownCostDO::getUnitId, sourceId);
        StartupShutdownCostDO startupShutdownCostDO = startupShutdownCostDOMapper.selectOne(eq);
        GeneratorDO generatorDO = generatorDOMapper.selectById(unitId);
        String value = String.format("%.2f", startupShutdownCostDO.getSpotCostMinoutput() / generatorDO.getMinP());
        result.put(Label.costOfClassicOfAnnualAndMonthly_basic, value);

        LambdaQueryWrapper<ThermalUnitOperatingCost> eq1 = new LambdaQueryWrapper<ThermalUnitOperatingCost>().eq(ThermalUnitOperatingCost::getUnitId, unitId);
        List<ThermalUnitOperatingCost> costs = thermalUnitOperatingCostMapper.selectList(eq1);
        Double costMin = costs.stream().min(Comparator.comparing(ThermalUnitOperatingCost::getSpotCostId)).orElseThrow(RuntimeException::new).getSpotCostMarginalCost();
        Double costMax = costs.stream().max(Comparator.comparing(ThermalUnitOperatingCost::getSpotCostId)).orElseThrow(RuntimeException::new).getSpotCostMarginalCost();
        String format = String.format("%s-%s", costMin, costMax);
        result.put(Label.costOfClassicOfAnnualAndMonthly_range, format);
        return result;
    }

    @GetMapping("costOfRenewable")
    public Map<Label, String> costOfRenewable(Integer unitId) {
        Map<Label, String> result = new LinkedHashMap<>();
        SprDO sprDO = sprMapper.selectList(null).get(0);
        String format = String.format("%.2f", sprDO.getRenewableGovernmentSubsidy());
        result.put(Label.costOfRenewable,format);
        return result;
    }

}
