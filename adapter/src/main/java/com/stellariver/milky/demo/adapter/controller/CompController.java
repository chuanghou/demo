package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.GeneratorDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.LoadDOMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompController {

    final DomainTunnel domainTunnel;
    final CompDOMapper compDOMapper;
    final GeneratorDOMapper generatorDOMapper;
    final LoadDOMapper loadDOMapper;

    @GetMapping("reset")
    public Result<Void> reset(@NotNull @Positive Integer agentNumber, @RequestHeader("token") String token) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        BizEx.trueThrow(agentNumber > 15, ErrorEnums.PARAM_FORMAT_WRONG.message("不允许超过15个交易员"));
        CompCommand.Create command = CompCommand.Create.builder().compId(1).agentTotal(agentNumber).build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }


    @PostMapping("start")
    public Result<Void> start(@RequestHeader("token") String token) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        CompCommand.Start command = CompCommand.Start.builder().compId(1).build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }


    @PostMapping("step")
    public Result<Void> step(@RequestHeader("token") String token) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        Comp comp = domainTunnel.getByAggregateId(Comp.class, "1");
        boolean notClosed = comp.getMarketStatus() != Status.MarketStatus.CLOSE;
        BizEx.trueThrow(notClosed, ErrorEnums.PARAM_FORMAT_WRONG.message("当前市场自动倒计时结束，无须手动控制"));
        CompCommand.Step command = CompCommand.Step.builder().compId(1).build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }

}
