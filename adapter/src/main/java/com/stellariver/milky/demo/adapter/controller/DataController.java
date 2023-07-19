package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.AgentConfig;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.Round;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.infrastructure.database.entity.*;
import com.stellariver.milky.demo.infrastructure.database.mapper.*;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
@RequiredArgsConstructor
@RequestMapping("dataController")
public class DataController {

    final SprMapper sprMapper;
    final TpbfsdMapper tpbfsdMapper;

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

        transferData.put("全省火电最小", Collect.transfer(transferSprDOs, SprDO::getMinThermalMw));
        transferData.put("全省火电可调", Collect.transfer(transferSprDOs, SprDO::getAdjustableThermalMw));

        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            transferData.put("全省新能源预测", Collect.transfer(transferSprDOs, SprDO::getAnnualRenewableForecast));
            transferData.put("全省负荷预测", Collect.transfer(transferSprDOs, SprDO::getAnnualLoadForecast));
        } else if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL || marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            transferData.put("全省新能源预测", Collect.transfer(transferSprDOs, SprDO::getDaRenewableForecast));
            transferData.put("全省负荷预测", Collect.transfer(transferSprDOs, SprDO::getMonthlyLoadForecast));
        } else if (marketType == MarketType.INTRA_SPOT_PROVINCIAL || marketType == MarketType.INTER_SPOT_PROVINCIAL) {
            transferData.put("全省新能源预测", Collect.transfer(transferSprDOs, SprDO::getDaRenewableForecast));
            transferData.put("全省负荷预测", Collect.transfer(transferSprDOs, SprDO::getDaLoadForecast));
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }

        LambdaQueryWrapper<TpbfsdDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TpbfsdDO::getRoundId, Round.ONE.getDbCode());
        queryWrapper.eq(TpbfsdDO::getProv, Province.TRANSFER.getDbCode());

        List<TpbfsdDO> transferTpbfsdDOS = tpbfsdMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(TpbfsdDO::getPrd))
                .collect(Collectors.toList());

        transferData.put("全省送电预测", Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getAnnualReceivingForecastMw));

        result.put("送电省96时段供需分析", transferData);


        List<SprDO> receiveSprDOs = sprDOs.stream()
                .filter(d -> Kit.eq(d.getProv(), Province.RECEIVER.getDbCode()))
                .sorted(Comparator.comparing(SprDO::getDt))
                .collect(Collectors.toList());

        Map<String, List<Double>> receiveData = new HashMap<>();
        receiveData.put("全省火电最小", Collect.transfer(receiveSprDOs, SprDO::getMinThermalMw));
        receiveData.put("全省火电可调", Collect.transfer(receiveSprDOs, SprDO::getAdjustableThermalMw));
        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
            receiveData.put("全省新能源预测", Collect.transfer(receiveSprDOs, SprDO::getAnnualRenewableForecast));
            receiveData.put("全省负荷预测", Collect.transfer(receiveSprDOs, SprDO::getAnnualLoadForecast));
        } else if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL || marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            receiveData.put("全省新能源预测", Collect.transfer(receiveSprDOs, SprDO::getMonthlyRenewableForecast));
            receiveData.put("全省负荷预测", Collect.transfer(receiveSprDOs, SprDO::getMonthlyLoadForecast));
        } else {
            receiveData.put("全省新能源预测", Collect.transfer(receiveSprDOs, SprDO::getDaRenewableForecast));
            receiveData.put("全省负荷预测", Collect.transfer(receiveSprDOs, SprDO::getDaLoadForecast));
        }

        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TpbfsdDO::getRoundId, Round.ONE.getDbCode());
        queryWrapper.eq(TpbfsdDO::getProv, Province.RECEIVER.getDbCode());

        transferTpbfsdDOS = tpbfsdMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(TpbfsdDO::getPrd))
                .collect(Collectors.toList());

        transferData.put("全省受电预测", Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getAnnualReceivingForecastMw));

        result.put("受电省96时段供需分析", receiveData);

        Map<String, List<Double>> linkData = new HashMap<>();
        linkData.put("受电目标上限", Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMaxAnnualReceivingMw));

        linkData.put("受电目标下限", Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMinAnnualReceivingMw));

        result.put("省间联络线图", linkData);

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
            lineChart.put("该阻塞区负荷预测", loadForecast);
            lineChart.put("该阻塞区新能源发电预测", renewableForecast);

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
    @GetMapping("listAgentInventory")
    public Map<String, Map<String, List<Double>>> listAgentInventory(@RequestHeader("token") String token,
                                                                     @RequestParam String maketTypeValue) {
        MarketType marketType = MarketType.valueOf(maketTypeValue);
        String userId = TokenUtils.getUserId(token);
        Comp comp = domainTunnel.getByAggregateId(Comp.class, "1");

        AgentConfig config = comp.getAgentConfigs().stream()
                .filter(agentConfig -> Objects.equals(agentConfig.getAgentId(), Integer.parseInt(userId)))
                .findFirst().orElseThrow(() -> new BizEx(ErrorEnums.PARAM_FORMAT_WRONG.message("该用户不在此次竞赛中")));

        Map<String, Map<String, List<Double>>> result = new HashMap<>();

        Pair<String, Map<String, List<Double>>> mapPair = loadGenerator(config.getGeneratorId0(), marketType);
        result.put(mapPair.getKey(), mapPair.getValue());

        mapPair = loadGenerator(config.getGeneratorId1(), marketType);
        result.put(mapPair.getKey(), mapPair.getValue());

        mapPair = loadLoad(config.getLoadId0());
        result.put(mapPair.getKey(), mapPair.getValue());


        mapPair = loadLoad(config.getLoadId1());
        result.put(mapPair.getKey(), mapPair.getValue());

        return result;
    }

    private Pair<String, Map<String, List<Double>>> loadGenerator(Integer generatorId, MarketType marketType) {
        GeneratorDO generatorDO = generatorDOMapper.selectById(generatorId);
        Map<String, List<Double>> map = new HashMap<>();
        List<Double> maxPs = new ArrayList<>();
        IntStream.range(0, 16).forEach(i -> maxPs.add(generatorDO.getMaxP()));
        if (generatorDO.getType() == 1) {
            map.put("该机组最大发电能力", maxPs);
        } else {
            LambdaQueryWrapper<RenewableUnitDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RenewableUnitDO::getUnitId, generatorId);
            List<Double> values;
            if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTRA_ANNUAL_PROVINCIAL) {
                values = renewableUnitDOMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(RenewableUnitDO::getPrd))
                        .map(RenewableUnitDO::getAnnualForecast).collect(Collectors.toList());
            } else if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL || marketType == MarketType.INTRA_MONTHLY_PROVINCIAL) {
                values = renewableUnitDOMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(RenewableUnitDO::getPrd))
                        .map(RenewableUnitDO::getMonthlyForecast).collect(Collectors.toList());
            } else if (marketType == MarketType.INTRA_SPOT_PROVINCIAL || marketType == MarketType.INTER_SPOT_PROVINCIAL) {
                values = renewableUnitDOMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(RenewableUnitDO::getPrd))
                        .map(RenewableUnitDO::getDaForecast).collect(Collectors.toList());
            } else {
                throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
            }
            map.put("该机组发电预测", values);
        }
        LambdaQueryWrapper<GeneratorOutputStateDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GeneratorOutputStateDO::getUnitId, generatorId);
        List<Double> baseMws = generatorOutputStateMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(GeneratorOutputStateDO::getPrd))
                .map(GeneratorOutputStateDO::getBaseMw).collect(Collectors.toList());
        map.put("该机组基数合同电量", baseMws);
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
        map.put("该负荷用电预测", baseMws);
        return Pair.of(loadDO.getLoadName(), map);
    }


}
