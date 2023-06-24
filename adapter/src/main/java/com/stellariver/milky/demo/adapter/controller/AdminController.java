package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.demo.adapter.controller.req.AddCompReq;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.command.CompBuild;
import com.stellariver.milky.demo.domain.tunnel.UserTunnel;
import com.stellariver.milky.domain.support.command.Command;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminController {

    final UniqueIdBuilder uniqueIdBuilder;

    final UserTunnel userTunnel;

    @PostMapping("addComp")
    public Result<Void> addComp(@RequestBody AddCompReq req, @RequestHeader("token") String token) {

        String userId = TokenUtils.getUserId(token);

        User user = userTunnel.getById(userId);
        BizEx.trueThrow(user.getRole() != Role.ADMIN, ErrorEnums.PARAM_FORMAT_WRONG.message("管理员才可以新建比赛"));

        CompBuild compBuild = CompBuild.builder()
                .compId(uniqueIdBuilder.get().toString())
                .date(req.getDate())
                .name(req.getName())
                .agents(req.getAgents())
                .build();

        CommandBus.accept(compBuild, new HashMap<>());

        return Result.success();
    }




}
