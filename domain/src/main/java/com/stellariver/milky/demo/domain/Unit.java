package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.domain.command.CentralizedBid;
import com.stellariver.milky.demo.domain.command.RealTimeBid;
import com.stellariver.milky.demo.domain.command.UnitBuild;
import com.stellariver.milky.demo.domain.event.CentralizedBidden;
import com.stellariver.milky.demo.domain.event.RealtimeBidden;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.command.ConstructorHandler;
import com.stellariver.milky.domain.support.command.MethodHandler;
import com.stellariver.milky.domain.support.context.Context;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Builder;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Unit extends AggregateRoot {

    UnitIdentify unitIdentify;
    Double capacity;
    Double bought;
    Double sold;

    @Override
    public String getAggregateId() {
        return unitIdentify.getUnitId();
    }

    @ConstructorHandler
    public static Unit build(UnitBuild unitBuild, Context context) {
        Unit unit = Convertor.INST.to(unitBuild);
        unit.setBought(0D);
        unit.setSold(0D);
        return unit;
    }


    @MethodHandler
    public void handle(CentralizedBid centralizedBid, Context context) {

        Transaction transaction = centralizedBid.getTransaction();

        Pod pod = context.getByAggregateId(Pod.class, unitIdentify.getPodId());


        boolean generatorSell = (PodType.GENERATOR == pod.getPodType()) && (transaction.getDirection() == Direction.SELL);
        boolean loadBuy = (PodType.LOAD == pod.getPodType()) && (transaction.getDirection() == Direction.BUY);

        BizEx.falseThrow(generatorSell || loadBuy, ErrorEnums.PARAM_FORMAT_WRONG.message("参数异常" + centralizedBid));

        CentralizedBidden event = null;
        Double balance = null;

        if (generatorSell) {
            balance = capacity + bought - sold;
        }

        if (loadBuy) {
            balance = capacity + sold - bought;
        }

        if (transaction.getQuantity() <= balance) {
            sold += transaction.getQuantity();
            event = Convertor.INST.to(centralizedBid);
        }

        if (event != null) {
            context.publish(event);
        }

    }


    @MethodHandler
    public void handle(RealTimeBid realTimeBid, Context context) {

        Transaction transaction = realTimeBid.getTransaction();

    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Unit to(UnitBuild unitBuild);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CentralizedBidden to(CentralizedBid centralizedBid);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        RealtimeBidden to(RealTimeBid realTimeBid);

    }

}
