package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.*;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.common.Typed;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.LogIn;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.client.po.UserAddPO;
import com.stellariver.milky.demo.client.po.UserEditPO;
import com.stellariver.milky.demo.client.vo.LogInVO;
import com.stellariver.milky.demo.client.vo.UserVO;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.command.UserLogin;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UserDOMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
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
    public Result<Void> addUser(@RequestBody UserAddPO userAddReq, @RequestHeader("token") String token) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        UserDO userDO = Convertor.INST.to(userAddReq);
        userDOMapper.insert(userDO);
        return Result.success();
    }

    @PostMapping("listUsers")
    public Result<List<UserVO>> listUsers(@RequestHeader("token") String token) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        List<UserDO> userDOs = userDOMapper.selectList(null);
        List<UserVO> userVOS = userDOs.stream().map(Convertor.INST::to).collect(Collectors.toList());
        return PageResult.success(userVOS);
    }

    @GetMapping("getUser")
    public Result<UserVO> getUser(@RequestHeader("token") String token) {
        UserDO userDO = userDOMapper.selectById(Integer.parseInt(TokenUtils.getUserId(token)));
        return Result.success(Convertor.INST.to(userDO));
    }

    @PostMapping("edit")
    public Result<Void> edit(@RequestHeader("token") String token, @RequestBody UserEditPO userEditPO) {
        UserDO userDO = userDOMapper.selectById(Integer.parseInt(TokenUtils.getUserId(token)));
        userDO.setName(userEditPO.getName());
        userDO.setPassword(userEditPO.getNewPassword());
        userDOMapper.updateById(userDO);
        return Result.success();
    }

    @GetMapping("login")
    public Result<LogInVO> login(@RequestParam String userId, @RequestParam String password) {
        UserDO userDO = userDOMapper.selectById(Integer.parseInt(userId));
        if (userDO == null || Kit.notEq(userDO.getPassword(), password)) {
            return Result.error(ErrorEnums.ACCOUNT_PASSWORD_ERROR.message("账号密码错误"), ExceptionType.BIZ);
        }
        LogInVO logInVO = new LogInVO(TokenUtils.sign(userId), userDO.getRole());
        return Result.success(logInVO);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        UserDO to(UserAddPO userAddReq);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        UserVO to(UserDO userDO);

        @AfterMapping
        default void after(UserDO userDO, UserVO userVO) {
            userVO.setRole(Role.valueOf(userVO.getRole()).getDesc());
        }

    }



}
