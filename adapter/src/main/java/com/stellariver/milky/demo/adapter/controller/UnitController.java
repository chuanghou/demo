package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.BeanUtil;
import com.stellariver.milky.demo.adapter.controller.req.AddPodReq;
import com.stellariver.milky.demo.adapter.controller.req.ListUnitsReq;
import com.stellariver.milky.demo.adapter.controller.resp.PodResp;
import com.stellariver.milky.demo.adapter.controller.resp.UnitResp;
import com.stellariver.milky.demo.basic.PodType;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.basic.UnitIdentify;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.entity.PodDO;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.PodDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.UnitDOMapper;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("pod")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitController {

    final UnitDOMapper unitDOMapper;
    final CompDOMapper compDOMapper;
    final PodDOMapper podDOMapper;

    @GetMapping("listUnits")
    public Result<List<UnitResp>> listUnits(@RequestBody ListUnitsReq listUnitsReq, @RequestHeader("token") String token) {
        String userId = TokenUtils.getUserId(token);

        String compId = listUnitsReq.getCompId();
        CompDO compDO = compDOMapper.selectById(compId);

        LambdaQueryWrapper<UnitDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UnitDO::getCompId, compId);
        lambdaQueryWrapper.eq(UnitDO::getUserId, userId);
        List<UnitDO> unitDOs = unitDOMapper.selectList(lambdaQueryWrapper);

        List<UnitIdentify> unitIdentifies = unitDOs.stream().map(UnitDO::getUnitId).map(UnitIdentify::resolve).collect(Collectors.toList());
        List<String> podIds = unitIdentifies.stream().map(UnitIdentify::getPodId).collect(Collectors.toList());
        Map<String, PodDO> podDOMap = podDOMapper.selectBatchIds(podIds).stream().collect(Collectors.toMap(PodDO::getPodId, Function.identity()));
        List<UnitResp> unitResps = unitDOs.stream().map(unitDO -> {
            String pId = UnitIdentify.resolve(unitDO.getUnitId()).getPodId();
            PodDO podDO = podDOMap.get(pId);
            boolean b = PodType.valueOf(podDO.getPodType()) == PodType.GENERATOR;
            double available = unitDO.getCapacity() + (b ? unitDO.getBought() - unitDO.getSold() : unitDO.getSold() - unitDO.getBought());
            return UnitResp.builder()
                    .unitId(unitDO.getUnitId())
                    .compId(compId)
                    .comName(compDO.getName())
                    .podId(podDO.getPodId())
                    .podName(podDO.getName())
                    .capacity(available)
                    .build();
        }).collect(Collectors.toList());
        return Result.success(unitResps);
    }



    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        PodResp to(PodDO podDO);

        @AfterMapping
        default void after(PodDO podDO, @MappingTarget PodResp podResp) {
            podResp.setPodType(PodType.valueOf(podDO.getPodType()).getDesc());
        }

        @BeanMapping(builder = @Builder(disableBuilder = true))
        PodDO to(AddPodReq addPodReq);

        @AfterMapping
        default void after(AddPodReq addPodReq, @MappingTarget PodDO podDO) {
            podDO.setPodId(BeanUtil.getBean(UniqueIdBuilder.class).get().toString());
        }

    }

}
