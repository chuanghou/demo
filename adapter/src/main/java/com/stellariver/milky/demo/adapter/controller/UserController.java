package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.BaseEx;
import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.PageResult;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.Typed;
import com.stellariver.milky.demo.adapter.controller.req.LoginReq;
import com.stellariver.milky.demo.adapter.controller.req.UserAddReq;
import com.stellariver.milky.demo.adapter.controller.resp.UserResp;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.domain.command.UserLogin;
import com.stellariver.milky.demo.domain.tunnel.DomainTunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UserDOMapper;
import com.stellariver.milky.domain.support.ErrorEnums;
import com.stellariver.milky.domain.support.command.CommandBus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    final DomainTunnel domainTunnel;
    final UserDOMapper userDOMapper;

    @PostMapping("addUser")
    public Result<Void> addUser(@RequestBody UserAddReq userAddReq, @RequestHeader("token") String token) {
        if (!domainTunnel.checkAdmin(TokenUtils.getUserId(token))) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        UserDO userDO = Convertor.INST.to(userAddReq);
        userDOMapper.insert(userDO);
        return Result.success();
    }

    @PostMapping("listUsers")
    public Result<List<UserResp>> listUsers(@RequestHeader("token") String token) {
        if (!domainTunnel.checkAdmin(TokenUtils.getUserId(token))) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        List<UserDO> userDOs = userDOMapper.selectList(null);
        List<UserResp> userResps = userDOs.stream().map(Convertor.INST::to).collect(Collectors.toList());
        return PageResult.success(userResps);
    }


    @GetMapping("login")
    public Result<String> update(@RequestBody LoginReq loginReq) {
        UserLogin userLogin = UserLogin.builder().userId(loginReq.getUserId()).password(loginReq.getPassword()).build();
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

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        UserDO to(UserAddReq userAddReq);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        UserResp to(UserDO userDO);

        @AfterMapping
        default void after(UserDO userDO, UserResp userResp) {
            userResp.setRole(Role.valueOf(userResp.getRole()).getDesc());
        }

    }



}
