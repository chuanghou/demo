package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.tool.wire.StaticWire;
import com.stellariver.milky.demo.basic.Agent;
import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.domain.command.CompBuild;
import com.stellariver.milky.demo.domain.command.CompStep;
import com.stellariver.milky.demo.domain.event.CompBuilt;
import com.stellariver.milky.demo.domain.event.CompStepped;
import com.stellariver.milky.demo.domain.tunnel.DomainTunnel;
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
public class Comp extends AggregateRoot {

    String compId;
    String date;
    String name;
    Stage stage;
    List<Agent> agents;
    @Override
    public String getAggregateId() {
        return compId;
    }

    @StaticWire
    static DomainTunnel podRepository;

    @ConstructorHandler
    public static Comp build(CompBuild compBuild, Context context) {
        Comp comp = Convertor.INST.to(compBuild);
        comp.setStage(Stage.Initializing);
        CompBuilt compBuilt = Convertor.INST.to(comp);
        context.publish(compBuilt);
        return comp;
    }


    @MethodHandler
    public void step(CompStep compStep, Context context) {
        Stage nextStage = Stage.valueOf(stage.getNextStage());
        CompStepped compStepped = CompStepped.builder().compId(compId).lastStage(stage).nextStage(nextStage).build();
        stage = Stage.valueOf(stage.getNextStage());
        context.publish(compStepped);
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Comp to(CompBuild compBuild);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CompBuilt to(Comp comp);

    }


}
