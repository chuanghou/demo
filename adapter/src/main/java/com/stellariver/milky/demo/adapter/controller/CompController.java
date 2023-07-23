package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.repository.domain.CompDODAOWrapper;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.client.po.CompEditPO;
import com.stellariver.milky.demo.common.GridLimit;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.PriceLimit;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.entity.MarketSettingDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.MarketSettingMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("comp")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompController {

    final DomainTunnel domainTunnel;
    final CompDOMapper compDOMapper;
    final UniqueIdBuilder uniqueIdBuilder;
    final MarketSettingMapper marketSettingMapper;


    @GetMapping("runningComp")
    public Result<Comp> runningComp() {

        Comp build = Comp.builder().compId(1L).compStatus(Status.CompStatus.OPEN).marketStatus(Status.MarketStatus.OPEN)
                .marketType(MarketType.INTER_ANNUAL_PROVINCIAL)
                .build();
        return Result.success(build);
//        LambdaQueryWrapper<CompDO> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.ne(CompDO::getCompStatus, Status.CompStatus.END);
//        List<CompDO> compDOs = compDOMapper.selectList(queryWrapper);
//        BizEx.trueThrow(compDOs.size() > 1, ErrorEnums.PARAM_FORMAT_WRONG.message("存在多个非关闭状态竞赛，请联系管理员"));
//        Comp comp = domainTunnel.getByAggregateId(Comp.class, compDOs.get(0).getCompId().toString());
//        return Result.success(comp);
    }

    @GetMapping("listComps")
    public Result<List<Comp>> listComps() {
        List<CompDO> compDOs= compDOMapper.selectList(null);
        List<Comp> comps = Collect.transfer(compDOs, CompDODAOWrapper.Convertor.INST::to);
        return Result.success(comps);
    }


    @GetMapping("create")
    public Result<Void> create(@RequestHeader("token") String token,
                               @RequestParam @NotNull @Positive Integer agentNumber) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        BizEx.trueThrow(agentNumber > 15, ErrorEnums.PARAM_FORMAT_WRONG.message("不允许超过15个交易员"));
        LambdaQueryWrapper<CompDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(CompDO::getCompStatus, Status.CompStatus.END);
        List<CompDO> compDOs = compDOMapper.selectList(queryWrapper);
        BizEx.trueThrow(compDOs.size() == 1, ErrorEnums.PARAM_FORMAT_WRONG.message("存在一个非关闭状态竞赛, 请关闭后再创建新的"));
        SysEx.trueThrow(compDOs.size() >= 1, ErrorEnums.PARAM_FORMAT_WRONG.message("存在多个非关闭状态竞赛, 请联系系统管理员"));
        Long compId = uniqueIdBuilder.get();
        MarketSettingDO marketSettingDO = marketSettingMapper.selectById(1);
        GridLimit generatorPriceLimit = GridLimit.builder().low(marketSettingDO.getOfferPriceFloor()).high(marketSettingDO.getOfferPriceCap()).build();
        GridLimit loadPriceLimit = GridLimit.builder().low(marketSettingDO.getBidPriceFloor()).high(marketSettingDO.getBidPriceCap()).build();
        PriceLimit priceLimit = PriceLimit.builder().generatorPriceLimit(generatorPriceLimit).loadPriceLimit(loadPriceLimit).build();
        CompCommand.Create command = CompCommand.Create.builder()
                .compId(compId)
                .agentTotal(agentNumber)
                .priceLimit(priceLimit)
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
    public Result<Void> step(@RequestHeader String token,
                             @RequestParam Long compId) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        Comp comp = domainTunnel.getByAggregateId(Comp.class, compId.toString());
        Stage nextStage = Stage.builder().roundId(comp.getRoundId())
                .marketType(comp.getMarketType()).marketStatus(comp.getMarketStatus()).build().next(comp.getRoundTotal());
        CompCommand.Step command = CompCommand.Step.builder()
                .compId(compId)
                .targetRoundId(nextStage.getRoundId())
                .targetMarketType(nextStage.getMarketType())
                .targetMarketStatus(nextStage.getMarketStatus())
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



}
