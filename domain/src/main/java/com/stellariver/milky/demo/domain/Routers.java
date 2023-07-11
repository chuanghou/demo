package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.domain.command.UnitEvent;
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

import java.util.List;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Routers implements EventRouters {

    final DomainTunnel domainTunnel;
    final Tunnel tunnel;
    final UniqueIdBuilder uniqueIdBuilder;

    @EventRouter
    public void route(CompEvent.Created created, Context context) {
        created.getAgents().forEach(agent -> {
            List<String> metaUniIds = agent.getMetaUnitIds();
            metaUniIds.forEach(metaUnitId -> {
                MetaUnit metaUnit = tunnel.getByMetaUnitId(metaUnitId);
                UnitCommand.UnitCreate command = UnitCommand.UnitCreate.builder()
                        .unitId(uniqueIdBuilder.get().toString())
                        .metaUnitId(metaUnit.getName())
                        .userId(agent.getUserId())
                        .quantities(metaUnit.getQuantities())
                        .compId(created.getCompId())
                        .build();
                CommandBus.driveByEvent(command, created);
            });
        });
    }

    @EventRouter
    public void route(CompEvent.Stepped stepped, Context context) {
       if (stepped.getNextStage() == Stage.STAGE_ONE_CLEARANCE) {
           CompCommand.Clear clear = CompCommand.Clear.builder().stage(Stage.STAGE_ONE_CLEARANCE)
                   .compId(stepped.getCompId())
                   .build();
           CommandBus.driveByEvent(clear, stepped);
       } else if (stepped.getNextStage() == Stage.STAGE_THREE_CLEARANCE) {
           CompCommand.Clear clear = CompCommand.Clear.builder().stage(Stage.STAGE_THREE_CLEARANCE)
                   .compId(stepped.getCompId())
                   .build();
           CommandBus.driveByEvent(clear, stepped);
       }
    }

    @EventRouter
    public void route(UnitEvent.RealtimeBidden event, Context context) {

        Bid bid = event.getBid();
        String compId = event.getCompId();

    }


}
