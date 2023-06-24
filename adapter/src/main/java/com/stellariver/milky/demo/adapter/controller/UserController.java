package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.BaseEx;
import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.Typed;
import com.stellariver.milky.demo.adapter.controller.req.LoginReq;
import com.stellariver.milky.demo.domain.command.UserLogin;
import com.stellariver.milky.domain.support.ErrorEnums;
import com.stellariver.milky.domain.support.command.CommandBus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {


    @GetMapping("login")
    public Result<String> update(@RequestBody LoginReq loginReq) {
        UserLogin userLogin = UserLogin.builder().userId(loginReq.getAgentId()).password(loginReq.getPassword()).build();
        Map<Class<? extends Typed<?>>, Object> parameters = new HashMap<>();
        String token = null;
        try {
            token = (String) CommandBus.accept(userLogin, parameters);
        } catch (BaseEx baseEx) {
            if (baseEx.getFirstError().getCode().equals(ErrorEnums.AGGREGATE_NOT_EXISTED.getCode())) {
                return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("账户不存在"), ExceptionType.BIZ);
            }
        }
        return Result.success(token);
    }



}
