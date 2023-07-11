package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.domain.command.CompClearStageOne;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.domain.event.CompCreated;
import com.stellariver.milky.demo.domain.event.CompStepped;
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

import java.util.List;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Routers implements EventRouters {

    final DomainTunnel domainTunnel;
    final Tunnel tunnel;
    final UniqueIdBuilder uniqueIdBuilder;

    @EventRouter
    public void route(CompCreated compCreated, Context context) {
        compCreated.getAgents().forEach(agent -> {
            List<String> metaUniIds = agent.getMetaUnitIds();
            metaUniIds.forEach(metaUnitId -> {
                MetaUnit metaUnit = tunnel.getByMetaUnitId(metaUnitId);
                UnitCommand.UnitCreate command = UnitCommand.UnitCreate.builder()
                        .unitId(uniqueIdBuilder.get().toString())
                        .metaUnitId(metaUnit.getName())
                        .userId(agent.getUserId())
                        .quantities(metaUnit.getQuantities())
                        .compId(compCreated.getCompId())
                        .build();
                CommandBus.driveByEvent(command, compCreated);
            });
        });
    }
}
