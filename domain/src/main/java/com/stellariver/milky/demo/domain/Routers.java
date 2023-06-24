package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.TimeFrame;
import com.stellariver.milky.demo.basic.UnitIdentify;
import com.stellariver.milky.demo.domain.command.CentralizedBidbuild;
import com.stellariver.milky.demo.domain.command.RealTimeBidCreate;
import com.stellariver.milky.demo.domain.command.UnitBuild;
import com.stellariver.milky.demo.domain.event.CentralizedBidden;
import com.stellariver.milky.demo.domain.event.CompBuilt;
import com.stellariver.milky.demo.domain.event.RealtimeBidden;
import com.stellariver.milky.demo.domain.tunnel.PodTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.domain.support.event.EventRouter;
import com.stellariver.milky.domain.support.event.EventRouters;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Routers implements EventRouters {

    final PodTunnel podRepository;

    @EventRouter
    public void route(CentralizedBidden bidden) {
        CentralizedBidbuild centralizedBidbuild = Convertor.INST.to(bidden);
        CommandBus.driveByEvent(centralizedBidbuild, bidden);
    }

    @EventRouter
    public void route(RealtimeBidden bidden) {
        RealTimeBidCreate realTimeBidCreate = Convertor.INST.to(bidden);
        CommandBus.driveByEvent(realTimeBidCreate, bidden);
    }

    @EventRouter
    public void route(CompBuilt compBuilt) {
        compBuilt.getAgents().forEach(agent -> {
            List<String> podIds = agent.getPodIds();
            podIds.forEach(podId -> {
                Pod pod = podRepository.queryById(podId);
                UnitIdentify baseUnitIdentify = UnitIdentify.builder()
                        .compId(compBuilt.getCompId())
                        .podId(podId)
                        .date(compBuilt.getDate())
                        .podType(pod.getPodType())
                        .build();
                UnitIdentify peakUnitIdentify = baseUnitIdentify.toBuilder().timeFrame(TimeFrame.PEAK).build();
                UnitBuild peakUnitBuild = UnitBuild.builder().unitIdentify(peakUnitIdentify).capacity(pod.getPeakCapacity()).build();
                CommandBus.driveByEvent(peakUnitBuild, compBuilt);

                UnitIdentify flatUnitIdentify = baseUnitIdentify.toBuilder().timeFrame(TimeFrame.PEAK).build();
                UnitBuild flatUnitBuild = UnitBuild.builder().unitIdentify(flatUnitIdentify).capacity(pod.getFlatCapacity()).build();
                CommandBus.driveByEvent(flatUnitBuild, compBuilt);

                UnitIdentify valleyUnitIdentify = baseUnitIdentify.toBuilder().timeFrame(TimeFrame.PEAK).build();
                UnitBuild valleyUnitBuild = UnitBuild.builder().unitIdentify(valleyUnitIdentify).capacity(pod.getPeakCapacity()).build();
                CommandBus.driveByEvent(valleyUnitBuild, compBuilt);
            });
        });
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CentralizedBidbuild to(CentralizedBidden bidden);


        @BeanMapping(builder = @Builder(disableBuilder = true))
        RealTimeBidCreate to(RealtimeBidden bidden);

    }
}
