package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.demo.adapter.controller.req.AddCompReq;
import com.stellariver.milky.demo.adapter.controller.req.StepCompReq;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.command.CompBuild;
import com.stellariver.milky.demo.domain.command.CompStep;
import com.stellariver.milky.demo.domain.tunnel.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("comp")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompController {

    final UniqueIdBuilder uniqueIdBuilder;
    final DomainTunnel domainTunnel;

    @PostMapping("addComp")
    public Result<Void> addComp(@RequestBody AddCompReq req, @RequestHeader("token") String token) {
        checkAdmin(token);

        Comp currentComp = domainTunnel.getCurrentComp();
        if (currentComp != null) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("当前仍有运行中的竞赛，请终止相应竞赛"), ExceptionType.BIZ);
        }

        CompBuild compBuild = CompBuild.builder()
                .compId(uniqueIdBuilder.get().toString())
                .date(req.getDate())
                .name(req.getName())
                .agents(req.getAgents())
                .build();

        CommandBus.accept(compBuild, new HashMap<>());

        return Result.success();
    }

   private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostMapping
    public Result<Void> stepComp(@RequestBody StepCompReq req, @RequestHeader("token") String token) {
        checkAdmin(token);
        Comp currentComp = domainTunnel.getCurrentComp();
        if (currentComp == null) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("当前无竞赛"), ExceptionType.BIZ);
        }
        Stage stage = currentComp.getStage();
        if (stage.getAutoForNext()) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("请等待自动结束"), ExceptionType.BIZ);
        }
        CompStep compStep = CompStep.builder().compId(currentComp.getCompId()).build();
        CommandBus.accept(compStep, new HashMap<>());
        scheduledExecutorService.schedule(() -> {
            CompStep autoCompStep = CompStep.builder().compId(currentComp.getCompId()).build();
            CommandBus.accept(autoCompStep, new HashMap<>());
        }, req.getLength(), TimeUnit.SECONDS);

        return Result.success();
    }



    @GetMapping("currentComp")
    public Result<String> queryCurrentComp(@RequestHeader("token") String token) {
        Comp currentComp = domainTunnel.getCurrentComp();
        if (currentComp == null) {
            return Result.success("当前系统没有开始的竞赛, 请等待管理员通知!");
        } else {
            return Result.success(currentComp.getCompId() + ":" + currentComp.getStage().getDesc());
        }
    }

    private void checkAdmin(String token) {
        String userId = TokenUtils.getUserId(token);
        User user = domainTunnel.getByUserId(userId);
        BizEx.trueThrow(user.getRole() != Role.ADMIN, ErrorEnums.PARAM_FORMAT_WRONG.message("管理员入口，禁止普通用户操作"));
    }



}
