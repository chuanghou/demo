package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.Typed;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.client.po.CompCreatePO;
import com.stellariver.milky.demo.client.po.StepCompPO;
import com.stellariver.milky.demo.client.vo.CompVO;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.command.CompCreate;
import com.stellariver.milky.demo.domain.command.CompStep;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("comp")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompController {

    final UniqueIdBuilder uniqueIdBuilder;
    final DomainTunnel domainTunnel;
    final CompDOMapper compDOMapper;

    @GetMapping
    public Result<List<CompVO>> listComps() {
        LambdaQueryWrapper<CompDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(CompDO::getStage, Stage.END.name());
        List<CompDO> compDOs = compDOMapper.selectList(queryWrapper);
        List<CompVO> compVOS = compDOs.stream().map(Convertor.INST::to).collect(Collectors.toList());
        return Result.success(compVOS);
    }

    @PostMapping("createComp")
    public Result<Void> createComp(@RequestBody CompCreatePO compCreatePO, @RequestHeader("token") String token) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));

        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }

        CompCreate command = CompCreate.builder()
                .agents(compCreatePO.getAgents())
                .compId(uniqueIdBuilder.get().toString())
                .name(compCreatePO.getName())
                .build();

        Map<Class<? extends Typed<?>>, Object> parameters = Collect.asMap(TypedEnums.USER_ID.class, user.getUserId());
        CommandBus.accept(command, parameters);
        return Result.success();
    }

   private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostMapping
    public Result<Void> stepComp(@RequestBody StepCompPO stepCompPO, @RequestHeader("token") String token) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));

        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        Comp comp = domainTunnel.getByAggregateId(Comp.class, stepCompPO.getCompId());
        BizEx.trueThrow(comp == null, ErrorEnums.PARAM_FORMAT_WRONG.message("对应竞赛不存在"));
        Stage stage = comp.getStage();
        if (stage.getAutoForNext()) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("请等待自动结束本阶段"), ExceptionType.BIZ);
        }
        CompStep compStep = CompStep.builder().compId(comp.getCompId()).build();
        CommandBus.accept(compStep, new HashMap<>());
        scheduledExecutorService.schedule(() -> {
            CompStep autoCompStep = CompStep.builder().compId(comp.getCompId()).build();
            CommandBus.accept(autoCompStep, new HashMap<>());
        }, stepCompPO.getLength(), TimeUnit.SECONDS);

        return Result.success();
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CompVO to(CompDO compDO);


    }

}
