package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.Enumeration;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.BeanUtil;
import com.stellariver.milky.demo.adapter.controller.req.AddPodReq;
import com.stellariver.milky.demo.adapter.controller.resp.AgentResp;
import com.stellariver.milky.demo.adapter.controller.resp.CompResp;
import com.stellariver.milky.demo.adapter.controller.resp.PodResp;
import com.stellariver.milky.demo.basic.Agent;
import com.stellariver.milky.demo.basic.PodType;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Pod;
import com.stellariver.milky.demo.domain.User;
import com.stellariver.milky.demo.domain.tunnel.DomainTunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.PodDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.PodDOMapper;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("pod")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PodController {

    final PodDOMapper podDOMapper;

    @GetMapping("listPods")
    public Result<List<PodResp>> listPods() {
        List<PodDO> podDOs = podDOMapper.selectList(null);
        List<PodResp> podResps = podDOs.stream().map(Convertor.INST::to).collect(Collectors.toList());
        return Result.success(podResps);
    }

    @GetMapping("addPod")
    public Result<List<PodResp>> listPods(AddPodReq addPodReq) {

        List<PodDO> podDOs = podDOMapper.selectList(null);
        List<PodResp> podResps = podDOs.stream().map(Convertor.INST::to).collect(Collectors.toList());
        return Result.success(podResps);
    }

    @GetMapping("listPodTypes")
    public Result<List<Enumeration>> listPodTypes() {
        List<Enumeration> enumerations = Arrays.stream(PodType.values())
                .map(podType -> new Enumeration(podType.name(), podType.getDesc())).collect(Collectors.toList());
        return Result.success(enumerations);
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
