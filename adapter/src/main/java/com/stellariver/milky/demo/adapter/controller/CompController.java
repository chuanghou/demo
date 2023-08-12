package com.stellariver.milky.demo.adapter.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.ErrorEnum;
import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.common.tool.util.StreamMap;
import com.stellariver.milky.demo.adapter.repository.domain.CompDODAOWrapper;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.client.po.CompCreatePO;
import com.stellariver.milky.demo.client.po.CompEditPO;
import com.stellariver.milky.demo.common.GridLimit;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.PriceLimit;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.entity.MarketSettingDO;
import com.stellariver.milky.demo.infrastructure.database.entity.TransLineLimitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.MarketSettingMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.TransLineLimitDOMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("comp")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompController {

    final Tunnel tunnel;
    final DomainTunnel domainTunnel;
    final CompDOMapper compDOMapper;
    final UniqueIdBuilder uniqueIdBuilder;
    final MarketSettingMapper marketSettingMapper;
    final TransLineLimitDOMapper transLineLimitDOMapper;


    @GetMapping("runningComp")
    public Result<Comp> runningComp() {
        Comp comp = tunnel.runningComp();
        return Result.success(comp);
    }

    @GetMapping("getDurations")
    public Result<Map<MarketType, Map<Status.MarketStatus, Integer>>> getDurations() {
        MarketSettingDO marketSettingDO = marketSettingMapper.selectById(1);
        Map<MarketType, Map<Status.MarketStatus, Integer>> data = new LinkedHashMap<>();

        Map<Status.MarketStatus, Integer> map0 = new LinkedHashMap<>();
        map0.put(Status.MarketStatus.OPEN, marketSettingDO.getIntraprovincialAnnualBidDuration());
        map0.put(Status.MarketStatus.CLOSE, marketSettingDO.getInterprovincialAnnualResultDuration());
        data.put(MarketType.INTER_ANNUAL_PROVINCIAL, map0);

        Map<Status.MarketStatus, Integer> map1  = new LinkedHashMap<>();
        map1.put(Status.MarketStatus.OPEN, marketSettingDO.getIntraprovincialAnnualBidDuration());
        map1.put(Status.MarketStatus.CLOSE, marketSettingDO.getIntraprovincialAnnualResultDuration());
        data.put(MarketType.INTRA_ANNUAL_PROVINCIAL, map1);

        Map<Status.MarketStatus, Integer> map2 = new LinkedHashMap<>();
        map2.put(Status.MarketStatus.OPEN, marketSettingDO.getInterprovincialMonthlyBidDuration());
        map2.put(Status.MarketStatus.CLOSE, marketSettingDO.getInterprovincialMonthlyResultDuration());
        data.put(MarketType.INTER_MONTHLY_PROVINCIAL, map2);

        Map<Status.MarketStatus, Integer> map3 = new LinkedHashMap<>();
        map3.put(Status.MarketStatus.OPEN, marketSettingDO.getIntraprovincialMonthlyBidDuration());
        map3.put(Status.MarketStatus.CLOSE, marketSettingDO.getIntraprovincialMonthlyResultDuration());
        data.put(MarketType.INTRA_MONTHLY_PROVINCIAL, map3);

        Map<Status.MarketStatus, Integer> map4 = new LinkedHashMap<>();
        map4.put(Status.MarketStatus.OPEN, marketSettingDO.getIntraprovincialSpotBidDuration());
        map4.put(Status.MarketStatus.CLOSE, marketSettingDO.getIntraprovincialSpotResultDuration());
        data.put(MarketType.INTRA_SPOT_PROVINCIAL, map4);

        Map<Status.MarketStatus, Integer> map5 = new LinkedHashMap<>();
        map5.put(Status.MarketStatus.OPEN, marketSettingDO.getInterprovincialSpotBidDuration());
        map5.put(Status.MarketStatus.CLOSE, marketSettingDO.getInterprovincialSpotResultDuration());
        data.put(MarketType.INTER_SPOT_PROVINCIAL, map5);

        Map<Status.MarketStatus, Integer> map6 = new LinkedHashMap<>();
        map6.put(Status.MarketStatus.OPEN, marketSettingDO.getSettleResultDuration());
        data.put(MarketType.FINAL_CLEAR, map6);

        return Result.success(data);
    }


    @PostMapping("create")
    public Result<Void> create(@RequestHeader("token") String token,
                               @RequestBody CompCreatePO compCreatePO) {

        Map<MarketType, Map<Status.MarketStatus, Integer>> durationParams = Json.parse(
                Json.toJson(compCreatePO.getDurations()),
                new TypeReference<Map<MarketType, Map<Status.MarketStatus, Integer>>>() {}
        );

        Map<MarketType, Map<Status.MarketStatus, Duration>> durations = new HashMap<>();
        durationParams.forEach((t, d) -> {
            Map<Status.MarketStatus, Duration> sd = new HashMap<>();
            d.forEach((s, l) -> sd.put(s, Duration.of(l, ChronoUnit.MINUTES)));
            durations.put(t, sd);
        });

        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        Long compId = uniqueIdBuilder.get();
        MarketSettingDO marketSettingDO = marketSettingMapper.selectById(1);
        GridLimit generatorPriceLimit = GridLimit.builder().low(marketSettingDO.getOfferPriceFloor()).high(marketSettingDO.getOfferPriceCap()).build();
        GridLimit loadPriceLimit = GridLimit.builder().low(marketSettingDO.getBidPriceFloor()).high(marketSettingDO.getBidPriceCap()).build();
        PriceLimit priceLimit = PriceLimit.builder().generatorPriceLimit(generatorPriceLimit).loadPriceLimit(loadPriceLimit).build();

        List<TransLineLimitDO> transLineLimitDOS = transLineLimitDOMapper.selectList(null);
        Map<MarketType, Map<TimeFrame, GridLimit>> marketTypeTransLimit = new HashMap<>();

        Map<TimeFrame, GridLimit> transLimit = new HashMap<>();
        for (TransLineLimitDO limitDO : transLineLimitDOS) {
            TimeFrame timeFrame = Kit.enumOfMightEx(TimeFrame::getDbCode, limitDO.getPfvPrd());
            GridLimit gridLimit = GridLimit.builder()
                    .low(limitDO.getMinAnnualReceivingMw())
                    .high(limitDO.getMaxAnnualReceivingMw())
                    .build();
            transLimit.put(timeFrame, gridLimit);
        }
        marketTypeTransLimit.put(MarketType.INTER_ANNUAL_PROVINCIAL, transLimit);

        transLimit = new HashMap<>();
        for (TransLineLimitDO limitDO : transLineLimitDOS) {
            TimeFrame timeFrame = Kit.enumOfMightEx(TimeFrame::getDbCode, limitDO.getPfvPrd());
            GridLimit gridLimit = GridLimit.builder()
                    .low(limitDO.getMinMonthlyReceivingMw())
                    .high(limitDO.getMaxMonthlyReceivingMw())
                    .build();
            transLimit.put(timeFrame, gridLimit);
        }
        marketTypeTransLimit.put(MarketType.INTER_MONTHLY_PROVINCIAL, transLimit);

        CompCommand.Create command = CompCommand.Create.builder()
                .compId(compId)
                .agentTotal(compCreatePO.getAgentNumber())
                .priceLimit(priceLimit)
                .transLimit(marketTypeTransLimit)
                .durations(durations)
                .build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }


    @PostMapping("start")
    public Result<Void> start(@RequestHeader String token,
                              @RequestParam Long compId) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        CompCommand.Start command = CompCommand.Start.builder().compId(compId).build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }

    @PostMapping("closeAll")
    public Result<Void> closeAll(@RequestHeader String token) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        List<CompDO> compDOS = compDOMapper.selectList(null);
        compDOS.forEach(compDO -> {
            compDO.setCompStatus(Status.CompStatus.END.name());
            compDOMapper.updateById(compDO);
        });
        return Result.success();
    }


    @PostMapping("edit")
    public Result<Void> edit(@RequestHeader String token,
                             @RequestBody CompEditPO compEditPO) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        List<Map<MarketType, Duration>> durations = compEditPO.getDurations().stream().map(mapPO -> {
            Map<MarketType, Duration> map = new HashMap<>();
            mapPO.forEach((key, value) -> map.put(MarketType.valueOf(key), Duration.of(value, ChronoUnit.CENTURIES)));
            return map;
        }).collect(Collectors.toList());
        CompCommand.Edit command = CompCommand.Edit.builder().compId(compEditPO.getCompId()).durations(durations).build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }


    @PostMapping("step")
    public Result<Void> step(@RequestHeader String token, @RequestParam Long compId) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        Comp runningComp = tunnel.runningComp();
        BizEx.nullThrow(runningComp, ErrorEnums.PARAM_FORMAT_WRONG.message("当前无运行中的比赛"));
        boolean equals = runningComp.getCompId().equals(compId);
        BizEx.falseThrow(equals, ErrorEnums.PARAM_FORMAT_WRONG.message("当前运行比赛为" + runningComp.getCompId()));
        Stage nextStage = Stage.builder().roundId(runningComp.getRoundId())
                .marketType(runningComp.getMarketType()).marketStatus(runningComp.getMarketStatus()).build().next();
        CompCommand.Step command = CompCommand.Step.builder()
                .compId(compId)
                .nextStage(nextStage)
                .build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }

    @PostMapping("close")
    public Result<Void> close(@RequestHeader String token,
                              @RequestParam Long compId) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        CompCommand.Close command = CompCommand.Close.builder().compId(compId).build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }


    @GetMapping("listRtCompVOs")
    public Result<List<RtCompVO>> listRtCompVOs(Integer roundId, String marketTypeValue) {
        Comp comp = tunnel.runningComp();
        List<RtCompVO> rtCompVOs = comp.getRtCompVOMap().values().stream().filter(rtCompVO -> {
            boolean b0 = Kit.eq(roundId, rtCompVO.getRoundId());
            boolean b1 = MarketType.valueOf(marketTypeValue) == rtCompVO.getMarketType();
            return b0 && b1;
        }).collect(Collectors.toList());
        return Result.success(rtCompVOs);
    }

    @GetMapping("listCentralizedDeals")
    public Result<Map<TimeFrame, CentralizedDeals>> listCentralizedDeals(Integer roundId, String marketTypeValue) {
        Comp comp = tunnel.runningComp();
        List<Map<MarketType, Map<TimeFrame, CentralizedDeals>>> roundCentralizedDeals = comp.getRoundCentralizedDeals();
        Map<TimeFrame, CentralizedDeals> centralizedDealsMap = roundCentralizedDeals.get(roundId).get(MarketType.valueOf(marketTypeValue));
        return Result.success(centralizedDealsMap) ;
    }



}
