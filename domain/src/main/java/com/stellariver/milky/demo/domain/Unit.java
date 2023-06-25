package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.domain.command.*;
import com.stellariver.milky.demo.domain.event.YepBidden;
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

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Unit extends AggregateRoot {

    UnitIdentify unitIdentify;
    PodPos podPos;
    PodType podType;
    String userId;
    Double capacity;
    Double bought;
    Double sold;
    List<Transaction> yipTransactions;
    List<Transaction> mipTransactions;

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
    public void handle(YepBid yepBid, Context context) {
        String userId = context.getMetaData(TypedEnums.USER_ID.class);
        BizEx.trueThrow(Kit.notEq(this.userId, userId), ErrorEnums.PARAM_FORMAT_WRONG.message("不能操作其他人的报单"));
        Pod pod = context.getByAggregateId(Pod.class, unitIdentify.getPodId());
        PodPos podPos = pod.getPodPos();
        PodType podType = pod.getPodType();
        boolean b0 = podPos == PodPos.TRANSFER && podType == PodType.GENERATOR;
        boolean b1 = podPos == PodPos.RECEIVE && podType == PodType.LOAD;
        BizEx.trueThrow((!b0) && (!b1), ErrorEnums.PARAM_FORMAT_WRONG.message("第一阶段机组只能报卖单，负荷只能报买单"));
        BizEx.trueThrow(b0 && yepBid.getDirection() == Direction.BUY, ErrorEnums.PARAM_FORMAT_WRONG.message("一阶段机组只能报卖单，负荷只能报买单"));
        BizEx.trueThrow(b1 && yepBid.getDirection() == Direction.SELL, ErrorEnums.PARAM_FORMAT_WRONG.message("一阶段机组只能报卖单，负荷只能报买单"));
        this.yipTransactions = yepBid.getTransactions();
        context.publish(Convertor.INST.to(yepBid));
    }

    @MethodHandler
    public void handle(YipBid yipBid, Context context) {

    }

    @MethodHandler
    public void handle(MepBid mepBid, Context context) {
        String userId = context.getMetaData(TypedEnums.USER_ID.class);
        BizEx.trueThrow(Kit.notEq(this.userId, userId), ErrorEnums.PARAM_FORMAT_WRONG.message("不能操作其他人的报单"));
        this.yipTransactions = mepBid.getTransactions();
    }

    @MethodHandler
    public void handle(MipBid mipBid, Context context) {

    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Unit to(UnitBuild unitBuild);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        YepBidden to(YepBid yepBid);

    }

}
