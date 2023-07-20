package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.domain.command.AgentCommand;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.domain.command.UnitEvent;
import com.stellariver.milky.demo.domain.event.AgentEvent;
import com.stellariver.milky.demo.domain.event.CompEvent;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.domain.support.context.Context;
import com.stellariver.milky.domain.support.event.EventRouter;
import com.stellariver.milky.domain.support.event.EventRouters;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Routers implements EventRouters {

    final DomainTunnel domainTunnel;
    final Tunnel tunnel;
    final UniqueIdBuilder uniqueIdBuilder;
    final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    @EventRouter
    public void route(CompEvent.Created event, Context context) {
        Long agentTotal = event.getComp().getAgentTotal();
        LongStream.range(0L, agentTotal).forEach(agentIndex -> {
            Long userId = agentIndex;
            List<Long> metaUnitIds = allocate(event.getComp().getRoundId(), userId);
            Long agentId = uniqueIdBuilder.get();
            AgentCommand.Create command = AgentCommand.Create.builder()
                    .agentId(agentId)
                    .compId(event.getCompId())
                    .roundId(event.getComp().getRoundId())
                    .userId(userId)
                    .metaUnitIds(metaUnitIds)
                    .build();
            CommandBus.driveByEvent(command, event);
        });
    }


    @EventRouter
    public void route(AgentEvent.Created event, Context context) {
        Agent agent = event.getAgent();
        agent.getMetaUnitIds().forEach(metaUnitId -> {
            UnitCommand.Create command = UnitCommand.Create.builder()
                    .unitId(uniqueIdBuilder.get())
                    .agentId(agent.getAgentId())
                    .compId(agent.getCompId())
                    .roundId(agent.getRoundId())
                    .metaUnitId(metaUnitId)
                    .build();
            CommandBus.driveByEvent(command, event);
        });
    }

    private List<Long> allocate(Long roundId, Long userId) {
        //TODO
        return Collect.asList(0L, 1L, 2L, 3L);
    }


    @EventRouter
    public void route(CompEvent.Stepped stepped, Context context) {
        Comp comp = context.getByAggregateId(Comp.class, stepped.getAggregateId());
        Duration duration = comp.getDurationMap().get(stepped.getNextMarketType());
        scheduledExecutorService.schedule(() -> {
            CompCommand.Close command = CompCommand.Close.builder().compId(1).build();
            CommandBus.accept(command, new HashMap<>());
        }, duration.getSeconds(), TimeUnit.SECONDS);
    }

    @EventRouter
    public void route(UnitEvent.RealtimeBidden event, Context context) {

        Bid bid = event.getBid();
        String compId = event.getCompId();

    }


}
