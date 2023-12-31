package com.stellariver.milky.demo.domain;


import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.LogIn;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.domain.command.UserEdit;
import com.stellariver.milky.demo.domain.command.UserLogin;
import com.stellariver.milky.demo.domain.event.UserEdited;

import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.command.MethodHandler;
import com.stellariver.milky.domain.support.context.Context;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AggregateRoot {

    Integer userId;
    Role role;
    String name;
    String password;

    @MethodHandler
    public LogIn login(UserLogin command, Context context) {
        BizEx.trueThrow(Kit.notEq(command.getPassword(), password),
                ErrorEnums.ACCOUNT_PASSWORD_ERROR.message("登陆密码错误!"));
        context.publishPlaceHolderEvent(getAggregateId());
        String token = TokenUtils.sign(userId.toString());
        return LogIn.builder().token(token).role(role).build();
    }

    @MethodHandler
    public void edit(UserEdit command, Context context) {
        boolean edited = false;
        UserEdited.UserEditedBuilder<?, ?> builder = UserEdited.builder();
        if (StringUtils.isNotBlank(command.getPassword())) {
            builder.oldPassword(password).newPassword(command.getPassword());
            password = command.getPassword();
            edited = true;
        }
        if (StringUtils.isNotBlank(command.getName())) {
            builder.oldName(name).newName(command.getName());
            name = command.getName();
            edited = true;
        }
        if (edited) {
            UserEdited event = builder.build();
            context.publish(event);
        }
    }




    @Override
    public String getAggregateId() {
        return userId.toString();
    }

}
