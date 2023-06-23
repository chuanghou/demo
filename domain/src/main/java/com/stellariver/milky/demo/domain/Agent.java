package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.domain.command.Login;
import com.stellariver.milky.domain.support.ErrorEnums;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.command.MethodHandler;
import com.stellariver.milky.domain.support.context.Context;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.awt.image.renderable.ContextualRenderedImageFactory;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Agent extends AggregateRoot {

    String id;
    String name;
    String password;


    @MethodHandler
    public String login(Login command, Context context) {
        BizEx.trueThrow(Kit.notEq(command.getPassword(), password),
                ErrorEnums.PARAM_FORMAT_WRONG.message("登陆密码错误!"));
        return TokenUtils.sign(id);
    }

    @Override
    public String getAggregateId() {
        return id;
    }
}
