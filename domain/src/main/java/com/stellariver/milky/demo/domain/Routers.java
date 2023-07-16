package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.domain.command.UnitEvent;
import com.stellariver.milky.demo.domain.event.CompEvent;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.context.Context;
import com.stellariver.milky.domain.support.event.EventRouter;
import com.stellariver.milky.domain.support.event.EventRouters;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Routers implements EventRouters {

    final DomainTunnel domainTunnel;
    final Tunnel tunnel;
    final UniqueIdBuilder uniqueIdBuilder;

    @EventRouter
    public void route(CompEvent.Stepped stepped, Context context) {
    }

    @EventRouter
    public void route(UnitEvent.RealtimeBidden event, Context context) {

        Bid bid = event.getBid();
        String compId = event.getCompId();

    }


}
