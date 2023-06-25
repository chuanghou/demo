package com.stellariver.milky.demo.adapter.controller.req;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.CustomValid;
import com.stellariver.milky.demo.basic.Agent;
import com.stellariver.milky.demo.basic.ErrorEnums;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddCompReq {

    @NotBlank
    String date;
    @NotBlank
    String name;
    @Valid
    List<Agent> agents;

    @CustomValid
    public void customValid() {
        long count = agents.stream().map(Agent::getUserId).distinct().count();
        BizEx.trueThrow(count != agents.size(), ErrorEnums.PARAM_FORMAT_WRONG.message("存在重复用户id!"));
        List<String> podIds = agents.stream().flatMap(agent -> agent.getPodIds().stream()).collect(Collectors.toList());
        BizEx.trueThrow(podIds.size() != new HashSet<>(podIds).size(), ErrorEnums.PARAM_FORMAT_WRONG.message("存在重复Pod Id!"));

    }

}
