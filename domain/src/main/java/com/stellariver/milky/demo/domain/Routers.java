package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.basic.TimeFrame;
import com.stellariver.milky.demo.basic.UnitIdentify;
import com.stellariver.milky.demo.domain.command.CompClear;
import com.stellariver.milky.demo.domain.command.UnitBuild;
import com.stellariver.milky.demo.domain.event.CompBuilt;
import com.stellariver.milky.demo.domain.event.CompStepped;
import com.stellariver.milky.demo.domain.tunnel.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.domain.support.context.Context;
import com.stellariver.milky.domain.support.event.EventRouter;
import com.stellariver.milky.domain.support.event.EventRouters;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Routers implements EventRouters {

    final DomainTunnel podRepository;

    @EventRouter
    public void route(CompBuilt compBuilt, Context context) {
        compBuilt.getAgents().forEach(agent -> {
            List<String> podIds = agent.getPodIds();
            podIds.forEach(podId -> {
                Pod pod = podRepository.getByPodId(podId);
                UnitIdentify baseUnitIdentify = UnitIdentify.builder()
                        .compId(compBuilt.getCompId())
                        .podId(podId)
                        .build();
                UnitIdentify peakUnitIdentify = baseUnitIdentify.toBuilder().timeFrame(TimeFrame.PEAK).build();
                UnitBuild peakUnitBuild = UnitBuild.builder()
                        .podPos(pod.getPodPos())
                        .podType(pod.getPodType())
                        .userId(agent.getUserId())
                        .unitIdentify(peakUnitIdentify)
                        .capacity(pod.getPeakCapacity())
                        .build();
                CommandBus.driveByEvent(peakUnitBuild, compBuilt);

                UnitIdentify flatUnitIdentify = baseUnitIdentify.toBuilder().timeFrame(TimeFrame.FLAT).build();
                UnitBuild flatUnitBuild = UnitBuild.builder()
                        .podPos(pod.getPodPos())
                        .podType(pod.getPodType())
                        .userId(agent.getUserId())
                        .unitIdentify(flatUnitIdentify)
                        .capacity(pod.getFlatCapacity())
                        .build();
                CommandBus.driveByEvent(flatUnitBuild, compBuilt);

                UnitIdentify valleyUnitIdentify = baseUnitIdentify.toBuilder().timeFrame(TimeFrame.VALLEY).build();
                UnitBuild valleyUnitBuild = UnitBuild.builder()
                        .podPos(pod.getPodPos())
                        .podType(pod.getPodType())
                        .userId(agent.getUserId())
                        .unitIdentify(valleyUnitIdentify)
                        .capacity(pod.getPeakCapacity())
                        .build();
                CommandBus.driveByEvent(valleyUnitBuild, compBuilt);
            });
        });
    }


    @EventRouter
    public void route(CompStepped compStepped, Context context) {
        Stage lastStage = compStepped.getLastStage();
        if (lastStage == Stage.STAGE_ONE_RUNNING) {
            CompClear compClear = CompClear.builder().compId(compStepped.getCompId()).stage(lastStage).build();
            CommandBus.driveByEvent(compClear, compStepped);
        }
    }
}
