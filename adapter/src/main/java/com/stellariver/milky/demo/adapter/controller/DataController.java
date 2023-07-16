package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.stellariver.milky.common.base.Enumeration;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.common.enums.Label;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.Round;
import com.stellariver.milky.demo.infrastructure.database.entity.SprDO;
import com.stellariver.milky.demo.infrastructure.database.entity.TpbfsdDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.SprMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.TpbfsdMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("dataController")
public class DataController {

    final SprMapper sprMapper;
    final TpbfsdMapper tpbfsdMapper;

    Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(100)
            .expireAfterWrite(3600, TimeUnit.SECONDS)
            .build();

    @GetMapping("listEnumerations")
    List<Enumeration> listEnumerations() throws ExecutionException {
        return Arrays.stream(Label.values()).map(e -> new Enumeration(e.name(), e.getDesc())).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @GetMapping("systemParameterRelease")
    Map<String, Map<String, List<Double>>> systemParameterRelease() throws ExecutionException {
        return (Map<String, Map<String, List<Double>>>) cache.get("systemParameterRelease", this::doLoad);
    }

    private Map<String, Map<String, List<Double>>> doLoad() {

        List<SprDO> sprDOs = sprMapper.selectList(null);

        Map<String, Map<String, List<Double>>> result = new HashMap<>();

        List<SprDO> transferSprDOs = sprDOs.stream()
                .filter(d -> Kit.eq(d.getProv(), Province.TRANSFER.getDbCode()))
                .sorted(Comparator.comparing(SprDO::getDt))
                .collect(Collectors.toList());

        Map<String, List<Double>> transferData = new HashMap<>();
        transferData.put(Label.min_thermal_mw.name(),
                Collect.transfer(transferSprDOs, SprDO::getMinThermalMw));
        transferData.put(Label.annual_renewable_forecast.name(),
                Collect.transfer(transferSprDOs, SprDO::getAnnualRenewableForecast));
        transferData.put(Label.adjustable_thermal_mw.name(),
                Collect.transfer(transferSprDOs, SprDO::getAdjustableThermalMw));
        transferData.put(Label.annual_load_forecast.name(),
                Collect.transfer(transferSprDOs, SprDO::getAnnualLoadForecast));

        LambdaQueryWrapper<TpbfsdDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TpbfsdDO::getRoundId, Round.ONE.getDbCode());
        queryWrapper.eq(TpbfsdDO::getProv, Province.TRANSFER.getDbCode());

        List<TpbfsdDO> transferTpbfsdDOS = tpbfsdMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(TpbfsdDO::getPrd))
                .collect(Collectors.toList());

        transferData.put(Label.annual_transfer_forecast_mw.name(),
                Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getAnnualReceivingForecastMw));

        result.put(Label.transfer_96_analysis.name(), transferData);


        List<SprDO> receiveSprDOs = sprDOs.stream()
                .filter(d -> Kit.eq(d.getProv(), Province.RECEIVER.getDbCode()))
                .sorted(Comparator.comparing(SprDO::getDt))
                .collect(Collectors.toList());

        Map<String, List<Double>> receiveData = new HashMap<>();
        receiveData.put(Label.min_thermal_mw.name(),
                Collect.transfer(receiveSprDOs, SprDO::getMinThermalMw));
        receiveData.put(Label.annual_renewable_forecast.name(),
                Collect.transfer(receiveSprDOs, SprDO::getAnnualRenewableForecast));
        receiveData.put(Label.adjustable_thermal_mw.name(),
                Collect.transfer(receiveSprDOs, SprDO::getAdjustableThermalMw));
        receiveData.put(Label.annual_load_forecast.name(),
                Collect.transfer(receiveSprDOs, SprDO::getAnnualLoadForecast));

        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TpbfsdDO::getRoundId, Round.ONE.getDbCode());
        queryWrapper.eq(TpbfsdDO::getProv, Province.RECEIVER.getDbCode());

        transferTpbfsdDOS = tpbfsdMapper.selectList(queryWrapper).stream()
                .sorted(Comparator.comparing(TpbfsdDO::getPrd))
                .collect(Collectors.toList());

        transferData.put(Label.annual_transfer_forecast_mw.name(),
                Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getAnnualReceivingForecastMw));

        result.put(Label.receiver_96_analysis.name(), receiveData);

        Map<String, List<Double>> linkData = new HashMap<>();
        linkData.put(Label.receive_target_upper_limit.name(),
                Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMaxAnnualReceivingMw));

        linkData.put(Label.receive_target_lower_limit.name(),
                Collect.transfer(transferTpbfsdDOS, TpbfsdDO::getMinAnnualReceivingMw));

        result.put(Label.inter_provincial_linking.name(), linkData);
        return result;
    }

}
