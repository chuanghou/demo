package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.domain.command.AgentEdit;
import com.stellariver.milky.demo.domain.command.AgentLogin;
import com.stellariver.milky.demo.domain.event.AgentEdited;
import com.stellariver.milky.domain.support.ErrorEnums;
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
public class Agent extends AggregateRoot {

    String id;
    String name;
    String password;


    @MethodHandler
    public String login(AgentLogin command, Context context) {
        BizEx.trueThrow(Kit.notEq(command.getPassword(), password),
                ErrorEnums.PARAM_FORMAT_WRONG.message("登陆密码错误!"));
        return TokenUtils.sign(id);
    }

    @MethodHandler
    public void edit(AgentEdit command, Context context) {
        boolean edited = false;
        AgentEdited.AgentEditedBuilder<?, ?> builder = AgentEdited.builder();
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
            AgentEdited event = builder.build();
            context.publish(event);
        }
    }


    @Override
    public String getAggregateId() {
        return id;
    }
}
