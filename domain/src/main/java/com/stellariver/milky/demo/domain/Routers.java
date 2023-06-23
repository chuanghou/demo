package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.domain.command.CentralizedBidCreate;
import com.stellariver.milky.demo.domain.command.RealTimeBidCreate;
import com.stellariver.milky.demo.domain.event.CentralizedBidden;
import com.stellariver.milky.demo.domain.event.RealtimeBidden;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.domain.support.event.EventRouter;
import com.stellariver.milky.domain.support.event.EventRouters;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Routers implements EventRouters {

    @EventRouter
    public void route(CentralizedBidden bidden) {
        CentralizedBidCreate centralizedBidCreate = Convertor.INST.to(bidden);
        CommandBus.driveByEvent(centralizedBidCreate, bidden);
    }

    @EventRouter
    public void route(RealtimeBidden bidden) {
        RealTimeBidCreate realTimeBidCreate = Convertor.INST.to(bidden);
        CommandBus.driveByEvent(realTimeBidCreate, bidden);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CentralizedBidCreate to(CentralizedBidden bidden);


        @BeanMapping(builder = @Builder(disableBuilder = true))
        RealTimeBidCreate to(RealtimeBidden bidden);

    }
}
