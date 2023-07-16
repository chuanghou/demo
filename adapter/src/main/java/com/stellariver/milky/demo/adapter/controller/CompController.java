package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.client.vo.CompVO;
import com.stellariver.milky.demo.common.MarketType;
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
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("comp")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompController {

    final DomainTunnel domainTunnel;
    final CompDOMapper compDOMapper;
    final GeneratorDOMapper generatorDOMapper;
    final LoadDOMapper loadDOMapper;

    @GetMapping
    public Result<Void> createComp(Integer agentNumber) {
        BizEx.trueThrow(agentNumber > 15, ErrorEnums.PARAM_FORMAT_WRONG.message("不允许超过15个交易员"));
        CompDO compDO = compDOMapper.selectById(1);
        compDO.setRoundId(null);
        compDO.setMarketType(null);
        compDO.setMarketStatus(null);

//        IntStream.range(1, agentNumber + 1).forEach(agentId -> {
//            IntStream.range(1, 4).forEach(roundId -> {
//                Pair<Integer, Integer> allocateIds = allocate(roundId, agentId);
//                AgentConfig.builder()
//                        .roundId(roundId)
//                        .agentId(agentId)
//                        .generatorIds(allocateIds)
//                        .loadIds(allocateIds)
//
//            });
//        });

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
        int i = userId - ( userCount / groupMemberCount ) * groupMemberCount;
        int j = unitCount / 6;
        return Pair.of( (pair.getLeft() - 1) * j + i, (pair.getRight() - 1) * j + i);
    }

    public static void main(String[] args) {
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 6; j++) {
                System.out.println(allocate(i, j, 5, 30));
            }
        }
    }

    @GetMapping
    public Result<Void> startComp() {
        CompCommand.Start command = CompCommand.Start.builder().compId(1).build();
        CommandBus.accept(command, new HashMap<>());
        return Result.success();
    }

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostMapping
    public Result<Void> stepComp(@RequestHeader("token") String token) {
        User user = domainTunnel.getByAggregateId(User.class, TokenUtils.getUserId(token));
        if (user.getRole() != Role.ADMIN) {
            return Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("需要管理员权限"), ExceptionType.BIZ);
        }
        CompCommand.Step command = CompCommand.Step.builder().compId(1).build();
        CommandBus.accept(command, new HashMap<>());

        return Result.success();
    }


    private Duration markingDuration(CompDO compDO, MarketType marketType) {
        return null;
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CompVO to(CompDO compDO);


    }

}
