package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.basic.AgentConfig;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.GeneratorDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.LoadDOMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompController {

    final DomainTunnel domainTunnel;
    final CompDOMapper compDOMapper;
    final GeneratorDOMapper generatorDOMapper;
    final LoadDOMapper loadDOMapper;

    @GetMapping("init")
    public Result<Void> init(@NotNull @Positive Integer agentNumber) {
        BizEx.trueThrow(agentNumber > 15, ErrorEnums.PARAM_FORMAT_WRONG.message("不允许超过15个交易员"));
        CompDO compDO = compDOMapper.selectById(1);
        Long loadCount = loadDOMapper.selectCount(null);
        Long generatorCount = generatorDOMapper.selectCount(null);
        long count = Math.min(loadCount, generatorCount) / 6 * 6;
        BizEx.trueThrow(count / 2 < agentNumber,  ErrorEnums.CONFIG_ERROR.message("数据库中机组或者负荷数量太少"));


        List<AgentConfig> agentConfigs = new ArrayList<>();

        IntStream.range(1, agentNumber + 1).forEach(agentId -> IntStream.range(1, 4).forEach(roundId -> {
            Pair<Integer, Integer> allocateIds = allocate(roundId, agentId, agentNumber, (int) count);
            AgentConfig agentConfig = AgentConfig.builder()
                    .roundId(roundId)
                    .agentId(agentId)
                    .generatorId0(allocateIds.getLeft())
                    .generatorId1(allocateIds.getRight())
                    .loadId0(allocateIds.getLeft())
                    .loadId1(allocateIds.getRight())
                    .build();
            agentConfigs.add(agentConfig);
        }));

        compDO.setAgentConfig(Json.toJson(agentConfigs));

        compDOMapper.updateById(compDO);
        return Result.success();
    }

    static Map<Integer, Pair<Integer, Integer>> roundOneMap = Collect.asMap(
            1, Pair.of(1, 6),
            2, Pair.of(2, 4),
            3, Pair.of(3, 5)
    );

    static Map<Integer, Pair<Integer, Integer>> roundTwoMap = Collect.asMap(
            1, Pair.of(2, 4),
            2, Pair.of(3, 5),
            3, Pair.of(1, 6)
    );

    static Map<Integer, Pair<Integer, Integer>> roundThreeMap = Collect.asMap(
            1, Pair.of(3, 5),
            2, Pair.of(1, 6),
            3, Pair.of(2, 4)
    );

    static Map<Integer, Map<Integer, Pair<Integer, Integer>>> alloacteMap = Collect.asMap(
            1, roundOneMap,
            2, roundTwoMap,
            3, roundThreeMap
    );

    private static Pair<Integer, Integer> allocate(Integer roundId, Integer userId, Integer userCount, Integer unitCount) {
        int groupMemberCount =  (userCount / 3) + (((userCount % 3) == 0) ? 0 : 1);
        Map<Integer, Pair<Integer, Integer>> integerPairMap = alloacteMap.get(roundId);
        int groupNumber = userId / groupMemberCount + (((userId % groupMemberCount) == 0) ? 0 : 1);
        Pair<Integer, Integer> pair = integerPairMap.get(groupNumber);
        int i = ((userId - 1) % groupMemberCount) + 1;
        int k = unitCount / 6;
        return Pair.of( (pair.getLeft() - 1) * k + i, (pair.getRight() - 1) * k + i);
    }


    @PostMapping("start")
    public Result<Void> start() {
        CompCommand.Start command = CompCommand.Start.builder().compId(1).build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }


    @PostMapping("step")
    public Result<Void> step(@RequestHeader("token") String token) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        Comp comp = domainTunnel.getByAggregateId(Comp.class, "1");
        boolean notClosed = comp.getMarketStatus() != Status.MarketStatus.CLOSE;
        BizEx.trueThrow(notClosed, ErrorEnums.PARAM_FORMAT_WRONG.message("当前市场自动倒计时结束，无须手动控制"));
        CompCommand.Step command = CompCommand.Step.builder().compId(1).build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }

}
