package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.domain.command.CompCommand;
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

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Routers implements EventRouters {

    final DomainTunnel domainTunnel;
    final Tunnel tunnel;
    final UniqueIdBuilder uniqueIdBuilder;
    final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @EventRouter
    public void route(CompEvent.Started started, Context context) {
        Comp comp = context.getByAggregateId(Comp.class, started.getAggregateId());
        Duration duration = comp.getDurationMap().get(started.getMarketType());
        scheduledExecutorService.schedule(() -> {
            CompCommand.Close command = CompCommand.Close.builder().compId(1).build();
            CommandBus.accept(command, new HashMap<>());
        }, duration.getSeconds(), TimeUnit.SECONDS);
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
