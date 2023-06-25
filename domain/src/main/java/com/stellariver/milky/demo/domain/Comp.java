package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.wire.StaticWire;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.domain.command.CompBuild;
import com.stellariver.milky.demo.domain.command.CompClear;
import com.stellariver.milky.demo.domain.command.CompStep;
import com.stellariver.milky.demo.domain.event.CompBuilt;
import com.stellariver.milky.demo.domain.event.CompCleared;
import com.stellariver.milky.demo.domain.event.CompStepped;
import com.stellariver.milky.demo.domain.tunnel.DomainTunnel;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.command.ConstructorHandler;
import com.stellariver.milky.domain.support.command.MethodHandler;
import com.stellariver.milky.domain.support.context.Context;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.Builder;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    Double iepMaxQuantity;
    Double iepMinQuantity;

    @Override
    public String getAggregateId() {
        return compId;
    }

    @StaticWire
    static DomainTunnel domainTunnel;

    @ConstructorHandler
    public static Comp build(CompBuild compBuild, Context context) {
        Comp comp = Convertor.INST.to(compBuild);
        comp.setStage(Stage.INITIALIZED);
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


    @MethodHandler
    public void clear(CompClear compClear, Context context) {
        Stage clearStage = compClear.getStage();
        CompCleared compCleared = CompCleared.builder().compId(compId).build();
        if (clearStage == Stage.STAGE_ONE_RUNNING) {
            clearStageOne();
        } else if (clearStage == Stage.STAGE_TWO_RUNNING) {
            clearStageTwo();
        } else if (clearStage == Stage.STAGE_THREE_RUNNING) {
            clearStageThree();
        } else if (clearStage == Stage.STAGE_FOUR_RUNNING) {
            clearStageFour();
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }
        context.publish(compCleared);
    }

    private void clearStageOne() {
        List<Unit> units = domainTunnel.getBuyCompId(compId);
        List<Unit> transferGenerators = units.stream()
                .filter(unit -> unit.getPodPos() == PodPos.TRANSFER && unit.getPodType() == PodType.GENERATOR)
                .collect(Collectors.toList());

        List<Unit> receiveLoads = units.stream()
                .filter(unit -> unit.getPodPos() == PodPos.RECEIVE && unit.getPodType() == PodType.LOAD)
                .collect(Collectors.toList());

        List<List<Unit>> groupedUnits = units.stream().collect(Collect.select(
                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.PEAK && unit.getPodPos() == PodPos.TRANSFER && unit.getPodType() == PodType.GENERATOR,
                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.PEAK && unit.getPodPos() == PodPos.RECEIVE && unit.getPodType() == PodType.LOAD,
                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.FLAT && unit.getPodPos() == PodPos.TRANSFER && unit.getPodType() == PodType.GENERATOR,
                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.FLAT && unit.getPodPos() == PodPos.RECEIVE && unit.getPodType() == PodType.LOAD,
                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.VALLEY && unit.getPodPos() == PodPos.TRANSFER && unit.getPodType() == PodType.GENERATOR,
                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.VALLEY && unit.getPodPos() == PodPos.RECEIVE && unit.getPodType() == PodType.LOAD
        ));


    }



    @Nullable
    private Pair<Double, Double> process(List<Unit> buyUnits, List<Unit> sellUnits) {

        List<Transaction> buyTrans = buyUnits.stream()
                .flatMap(unit -> unit.getYipTransactions().stream())
                .sorted(Comparator.comparing(Transaction::getPrice))
                .collect(Collectors.toList());
        List<PointSection> buyPointSections = new ArrayList<>();
        Double cumulateQuantity = 0D;
        for (Transaction transaction : buyTrans) {
            PointSection pointSection = PointSection.builder()
                    .direction(Direction.BUY)
                    .point(cumulateQuantity)
                    .value(transaction.getPrice())
                    .build();
            buyPointSections.add(pointSection);
            cumulateQuantity += transaction.getQuantity();
        }

        List<Transaction> sellTrans = sellUnits.stream()
                .flatMap(unit -> unit.getYipTransactions().stream())
                .sorted(Comparator.comparing(Transaction::getPrice).reversed())
                .collect(Collectors.toList());
        List<PointSection> sellPointSections = new ArrayList<>();
        cumulateQuantity = 0D;
        for (Transaction transaction : sellTrans) {
            PointSection pointSection = PointSection.builder()
                    .direction(Direction.SELL)
                    .point(cumulateQuantity)
                    .value(transaction.getPrice())
                    .build();
            sellPointSections.add(pointSection);
            cumulateQuantity += transaction.getQuantity();
        }

        List<PointSection> pointSections = Stream.of(buyPointSections, sellPointSections)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(PointSection::getPoint))
                .collect(Collectors.toList());

        Iterator<PointSection> iterator = pointSections.iterator();
        PointSection ps = iterator.next(), nextPs;
        Double interQuantity = null, interPrice = null;
        while (iterator.hasNext()) {
            nextPs = iterator.next();
            if (nextPs.getDirection() == ps.getDirection()) {
                ps = nextPs;
            } else {
                if (nextPs.getDirection() == Direction.SELL) {
                    if (nextPs.getValue() <= ps.getValue()) {
                        interQuantity = nextPs.getPoint();
                        interPrice = nextPs.getValue();
                    }
                } else {
                    if (nextPs.getValue() >= ps.getValue()) {
                        interQuantity = nextPs.getPoint();
                        interPrice = nextPs.getValue();
                    }
                }
            }
        }

        if (interQuantity == null && interPrice == null) {
           return null;
        } else {
            return Pair.of(interQuantity, interPrice);
        }

    }


    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static public class PointSection {
        Direction direction;
        Double point;
        Double value;
    }




    private void clearStageTwo() {

    }

    private void clearStageThree() {

    }

    private void clearStageFour() {

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
