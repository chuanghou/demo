package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.BeanUtil;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.adapter.controller.req.AddCompReq;
import com.stellariver.milky.demo.adapter.controller.req.StepCompReq;
import com.stellariver.milky.demo.adapter.controller.resp.AgentResp;
import com.stellariver.milky.demo.adapter.controller.resp.CompResp;
import com.stellariver.milky.demo.adapter.controller.resp.PodResp;
import com.stellariver.milky.demo.adapter.repository.domain.CompDAOAdapter;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Pod;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.command.CompBuild;
import com.stellariver.milky.demo.domain.command.CompStep;
import com.stellariver.milky.demo.domain.tunnel.DomainTunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.entity.PodDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.PodDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.UserDOMapper;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapping;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    final UserDOMapper userDOMapper;
    final PodDOMapper podDOMapper;

    @GetMapping
    public Result<List<CompResp>> listComps() {
        LambdaQueryWrapper<CompDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(CompDO::getStage, Stage.END.name());
        List<CompDO> compDOs = compDOMapper.selectList(queryWrapper);
        List<CompResp> compResps = compDOs.stream().map(Convertor.INST::to).collect(Collectors.toList());
        return Result.success(compResps);
    }

    @PostMapping("addComp")
    public Result<Void> addComp(@RequestBody AddCompReq req, @RequestHeader("token") String token) {
        if (!domainTunnel.checkAdmin(TokenUtils.getUserId(token))) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }

        List<String> userIds = req.getAgents().stream().map(Agent::getUserId).collect(Collectors.toList());
        boolean b0 = userDOMapper.selectBatchIds(userIds).size() == userIds.size();
        BizEx.falseThrow(b0, ErrorEnums.PARAM_FORMAT_WRONG.message("找不到对应的用户"));
        List<String> podIds = req.getAgents().stream().flatMap(agent -> agent.getPodIds().stream()).collect(Collectors.toList());
        boolean b1 = podDOMapper.selectBatchIds(podIds).size() == podIds.size();
        BizEx.falseThrow(b1, ErrorEnums.PARAM_FORMAT_WRONG.message("找不到对应的Pod"));


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
        if (!domainTunnel.checkAdmin(TokenUtils.getUserId(token))) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        Comp comp = domainTunnel.getByCompId(req.getCompId());
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
        }, req.getLength(), TimeUnit.SECONDS);

        return Result.success();
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CompResp to(CompDO compDO);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        default List<AgentResp> to(String value) {
            List<Agent> agents = Json.parseList(value, Agent.class);
            UserDOMapper userDOMapper = BeanUtil.getBean(UserDOMapper.class);
            PodDOMapper podDOMapper = BeanUtil.getBean(PodDOMapper.class);
            return agents.stream().map(agent -> AgentResp.builder().userId(agent.getUserId())
                    .userName(userDOMapper.selectById(agent.getUserId()).getName())
                    .pods(agent.getPodIds().stream().map(podId -> {
                        PodDO podDO = podDOMapper.selectById(podId);
                        return PodResp.builder()
                                .podId(podId)
                                .podType(PodType.valueOf(podDO.getPodType()).getDesc())
                                .podName(podDO.getName())
                                .peakCapacity(podDO.getPeakCapacity())
                                .flatCapacity(podDO.getFlatCapacity())
                                .valleyCapacity(podDO.getValleyCapacity())
                                .build();
                    }).collect(Collectors.toList()))
                    .build()).collect(Collectors.toList());
        }

        @AfterMapping
        default void after(Agent agent, AgentResp agentResp) {
            User user = BeanUtil.getBean(DomainTunnel.class).getByUserId(agent.getUserId());
            agentResp.setUserName(user.getName());
        }


        @BeanMapping(builder = @Builder(disableBuilder = true))
        default List<PodResp> to(List<String> podIds) {
            return podIds.stream().map(podId -> {
                DomainTunnel domainTunnel = BeanUtil.getBean(DomainTunnel.class);
                Pod pod = domainTunnel.getByPodId(podId);
                return PodResp.builder().podId(podId).podName(pod.getName()).build();
            }).collect(Collectors.toList());
        }

    }

}
