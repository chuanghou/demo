package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.wire.StaticWire;
import com.stellariver.milky.demo.basic.BidWrapper;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.common.enums.Agent;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.domain.command.CompCreate;
import com.stellariver.milky.demo.domain.command.CompStep;
import com.stellariver.milky.demo.domain.event.CompCreated;
import com.stellariver.milky.demo.domain.event.CompStepped;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.base.DomainTunnel;
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
import java.util.function.Function;
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

    Pair<Double, Double> peakTransferLimitation;
    Pair<Double, Double> flatTransferLimitation;
    Pair<Double, Double> valleyTransferLimitation;

    Map<TimeFrame, Double> replenishMap = new HashMap<>();

    Map<TimeFrame, RealtimeBidProcessor> realtimeBidProcessors = new HashMap<>();

    @Override
    public String getAggregateId() {
        return compId;
    }

    @StaticWire
    static DomainTunnel domainTunnel;
    @StaticWire
    static Tunnel unitTunnel;

    @ConstructorHandler
    public static Comp create(CompCreate compCreate, Context context) {
        Comp comp = Convertor.INST.to(compCreate);
        comp.setStage(Stage.INITIALIZED);
        CompCreated compCreated = Convertor.INST.to(comp);
        context.publish(compCreated);
        return comp;
    }

    @MethodHandler
    public void handle(CompStep compStep, Context context) {
        Stage nextStage = Stage.valueOf(stage.getNextStage());
        CompStepped compStepped = CompStepped.builder().compId(compId).lastStage(stage).nextStage(nextStage).build();
        stage = Stage.valueOf(stage.getNextStage());
        context.publish(compStepped);
    }


//    @MethodHandler
//    public void handle(CompClear compClear, Context context) {
//        Stage clearStage = compClear.getStage();
//        CompCleared compCleared;
//        if (clearStage == Stage.STAGE_ONE_RUNNING) {
//            compCleared = clearStageOne();
//        } else if (clearStage == Stage.STAGE_TWO_RUNNING) {
//            compCleared = clearStageTwo();
//        } else if (clearStage == Stage.STAGE_THREE_RUNNING) {
//            compCleared = clearStageThree();
//        } else if (clearStage == Stage.STAGE_FOUR_RUNNING) {
//            compCleared = clearStageFour();
//        } else {
//            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
//        }
//        context.publish(compCleared);
//    }

//    private CompCleared clearStageOne() {
//
//        List<Unit> units = unitTunnel.getByCompId(compId);
//
//        units = units.stream().filter(unit -> {
//            boolean b0 = (unit.getPosition() == Position.TRANSFER) && (unit.getUnitType() == UnitType.GENERATOR);
//            boolean b1 = (unit.getPosition() == Position.RECEIVE) && (unit.getUnitType() == UnitType.LOAD);
//            return b0 || b1;
//        }).collect(Collectors.toList());
//
//        List<List<Unit>> groupedUnits = units.stream().collect(Collect.select(
//                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.PEAK && unit.getPosition() == Position.RECEIVE && unit.getUnitType() == UnitType.LOAD,
//                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.PEAK && unit.getPosition() == Position.TRANSFER && unit.getUnitType() == UnitType.GENERATOR,
//                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.FLAT && unit.getPosition() == Position.RECEIVE && unit.getUnitType() == UnitType.LOAD,
//                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.FLAT && unit.getPosition() == Position.TRANSFER && unit.getUnitType() == UnitType.GENERATOR,
//                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.VALLEY && unit.getPosition() == Position.RECEIVE && unit.getUnitType() == UnitType.LOAD,
//                unit -> unit.getUnitIdentify().getTimeFrame() == TimeFrame.VALLEY && unit.getPosition() == Position.TRANSFER && unit.getUnitType() == UnitType.GENERATOR
//        ));
//
//        Function<Unit, Stream<TransactionWrapper>> wrapper = u -> u.getYipTransactions().stream().map(t -> TransactionWrapper.wrapper(t, u.getUnitIdentify()));
//        Function<List<Unit>, List<TransactionWrapper>> transformer = us -> us.stream().flatMap(wrapper).collect(Collectors.toList());
//        List<List<TransactionWrapper>> groupedTransactionWrappers= Collect.transfer(groupedUnits, transformer);
//
//        List<ExecuteBid> executeBids0 = resolveExecuteBids(groupedTransactionWrappers.get(0), groupedTransactionWrappers.get(1), peakTransferLimitation, TimeFrame.PEAK);
//        List<ExecuteBid> executeBids1 = resolveExecuteBids(groupedTransactionWrappers.get(2), groupedTransactionWrappers.get(3), flatTransferLimitation, TimeFrame.FLAT);
//        List<ExecuteBid> executeBids2 = resolveExecuteBids(groupedTransactionWrappers.get(4), groupedTransactionWrappers.get(5), valleyTransferLimitation, TimeFrame.VALLEY);
//
//        List<ExecuteBid> executeBids = Stream.of(executeBids0, executeBids1, executeBids2).flatMap(Collection::stream).collect(Collectors.toList());
//        return CompCleared.builder()
//                .compId(compId)
//                .executeBids(executeBids)
//                .build();
//    }

    /**
     * |
     * |__  buy
     * |  |_____
     * |        |__|_
     * |      _____|
     * |_____| sell
     * |------------------------------------------------
     * |
     */
//    private List<ExecuteBid> resolveExecuteBids(List<TransactionWrapper> buyTransactionWrappers,
//                                                List<TransactionWrapper> sellTransactionWrappers,
//                                                Pair<Double, Double> limitation,
//                                                TimeFrame timeFrame) {
//        buyTransactionWrappers = buyTransactionWrappers.stream()
//                .sorted(Comparator.comparing(TransactionWrapper::getPrice).reversed()).collect(Collectors.toList());
//        sellTransactionWrappers = sellTransactionWrappers.stream()
//                .sorted(Comparator.comparing(Transaction::getPrice)).collect(Collectors.toList());
//        List<PointLine> buyPointLines = buildPointLine(buyTransactionWrappers);
//        List<PointLine> sellPointLines = buildPointLine(sellTransactionWrappers);
//
//        Function<Double, Double> buyFunction = buildFx(buyPointLines);
//        Function<Double, Double> sellFunction = buildFx(buyPointLines);
//
//        Pair<Double, Double> interPoint;
//        if (buyPointLines.get(0).getPrice() < sellPointLines.get(0).getPrice()) {
//           interPoint = null;
//        } else {
//            interPoint = analyzeInterPoint(buyPointLines, sellPointLines);
//            if (interPoint == null) {
//                Double transferTotalQuantity = buyTransactionWrappers.stream().map(Transaction::getQuantity).reduce(0D, Double::sum);
//                Double receiveTotalQuantity = sellTransactionWrappers.stream().map(Transaction::getQuantity).reduce(0D, Double::sum);
//                double cumulateQuantity = Math.min(transferTotalQuantity, receiveTotalQuantity);
//                Double buyPrice = buyFunction.apply(cumulateQuantity);
//                Double sellPrice = sellFunction.apply(cumulateQuantity);
//                interPoint = Pair.of(cumulateQuantity, (buyPrice + sellPrice) / 2);
//            }
//        }
//
//        Double minTransfer = limitation.getLeft();
//        Double maxTransfer = limitation.getLeft();
//
//        Triple<Double, Double, Double> triple;
//
//        if (interPoint == null) {
//            triple = Triple.of(0D, null, null);
//        } else if (interPoint.getLeft() <= minTransfer) {
//            triple = Triple.of(interPoint.getLeft(), interPoint.getRight(), minTransfer - interPoint.getLeft());
//        } else if (interPoint.getLeft() > minTransfer && interPoint.getLeft() <= maxTransfer) {
//            triple =  Triple.of(interPoint.getLeft(), interPoint.getRight(), null);
//        } else if (interPoint.getLeft() > maxTransfer) {
//            double price = buyFunction.apply(maxTransfer) + sellFunction.apply(maxTransfer) / 2;
//            triple = Triple.of(maxTransfer, price, null);
//        } else {
//            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
//        }
//
//        UnitIdentify unitIdentify;
//        Double dealPrice;
//        Double dealQuantity;
//        List<ExecuteBid> executeBids = new ArrayList<>();
//
//        for (PointLine pointLine : buyPointLines) {
//            if (pointLine.getCumulateQuantity() + pointLine.getWidthQuantity() < triple.getLeft()) {
//                unitIdentify = pointLine.getUnitIdentify();
//                dealQuantity = pointLine.getWidthQuantity();
//                dealPrice = triple.getMiddle();
//            } else if (pointLine.getCumulateQuantity() < triple.getLeft()){
//                unitIdentify = pointLine.getUnitIdentify();
//                dealQuantity = triple.getLeft() - pointLine.getCumulateQuantity();
//                dealPrice = triple.getMiddle();
//            } else {
//                break;
//            }
//            Transaction transaction = Transaction.builder().price(dealPrice).quantity(dealQuantity).direction(Direction.BUY).build()
//            ExecuteBid executeBid = ExecuteBid.builder().unitIdentify(unitIdentify).transaction(transaction).build();
//            executeBids.add(executeBid);
//        }
//
//
//        for (PointLine pointLine : sellPointLines) {
//            if (pointLine.getCumulateQuantity() + pointLine.getWidthQuantity() < triple.getLeft()) {
//                unitIdentify = pointLine.getUnitIdentify();
//                dealQuantity = pointLine.getWidthQuantity();
//                dealPrice = triple.getMiddle();
//            } else if (pointLine.getCumulateQuantity() < triple.getLeft()){
//                unitIdentify = pointLine.getUnitIdentify();
//                dealQuantity = triple.getLeft() - pointLine.getCumulateQuantity();
//                dealPrice = triple.getMiddle();
//            } else {
//                break;
//            }
//            Transaction transaction = Transaction.builder().price(dealPrice).quantity(dealQuantity).direction(Direction.SELL).build();
//            ExecuteBid executeBid = ExecuteBid.builder().unitIdentify(unitIdentify).transaction(transaction).build();
//            executeBids.add(executeBid);
//        }
//
//        replenishMap.put(timeFrame, triple.getRight());
//
//        return executeBids;
//
//    }


    private Function<Double, Double> buildFx(List<PointLine> pointLines) {
        return x -> {
            for (PointLine pointLine : pointLines) {
                if (x <= pointLine.getCumulateQuantity() + pointLine.getWidthQuantity()) {
                    return pointLine.getPrice();
                }
            }
            return null;
        };
    }

    static public List<PointLine> buildPointLine(List<BidWrapper> bidWrappers) {
        List<PointLine> pointLines = new ArrayList<>();
        Double cumulateQuantity = 0D;
        for (BidWrapper bidWrapper : bidWrappers) {
            PointLine pointLine = PointLine.builder()
                    .direction(bidWrapper.getDirection())
                    .cumulateQuantity(cumulateQuantity)
                    .price(bidWrapper.getPrice())
                    .widthQuantity(bidWrapper.getQuantity())
                    .txGroup(bidWrapper.getTxGroup())
                    .build();
            pointLines.add(pointLine);
            cumulateQuantity += bidWrapper.getQuantity();
        }
        return pointLines;
    }



    @Nullable
    static public Pair<Double, Double> analyzeInterPoint(List<PointLine> buyPointLines, List<PointLine> sellPointLines) {

        List<PointLine> pointLines = Stream.of(buyPointLines, sellPointLines).flatMap(Collection::stream)
                .sorted(Comparator.comparing(PointLine::getCumulateQuantity)).collect(Collectors.toList());

        Iterator<PointLine> iterator = pointLines.iterator();
        PointLine ps = iterator.next(), nextPs;
        Double interQuantity = null, interPrice = null;


        while (iterator.hasNext()) {
            nextPs = iterator.next();
            if (nextPs.getDirection() != ps.getDirection()) {
                if (nextPs.getDirection() == Direction.SELL) {
                    if (nextPs.getPrice() > ps.getPrice()) {
                        interQuantity = nextPs.getCumulateQuantity();
                        if (Objects.equals(ps.getCumulateQuantity(), nextPs.getCumulateQuantity())) {
                            interPrice = (ps.getPrice() + nextPs.getPrice()) / 2;
                        } else {
                            interPrice = ps.getPrice();
                        }
                    } else if (Objects.equals(nextPs.getPrice(), ps.getPrice())) {
                        double p0 = ps.getCumulateQuantity() + ps.getWidthQuantity();
                        double p1 = nextPs.getCumulateQuantity() + nextPs.getWidthQuantity();
                        interQuantity = Math.min(p0, p1);
                        interPrice = ps.getPrice();
                    }
                } else if (nextPs.getDirection() == Direction.BUY){
                    if (nextPs.getPrice() < ps.getPrice()) {
                        interQuantity = nextPs.getCumulateQuantity();
                        if (Objects.equals(nextPs.getCumulateQuantity(), ps.getCumulateQuantity())) {
                            interPrice = (ps.getPrice() + nextPs.getPrice())/2;
                        } else {
                            interPrice = ps.getPrice();
                        }
                    } else if (Objects.equals(nextPs.getPrice(), ps.getPrice())){
                        double p0 = ps.getCumulateQuantity() + ps.getWidthQuantity();
                        double p1 = nextPs.getCumulateQuantity() + nextPs.getWidthQuantity();
                        interQuantity = Math.min(p0, p1);
                        interPrice = ps.getPrice();
                    }
                } else {
                    throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
                }
                ps = nextPs;
            }
        }

        if (interQuantity == null && interPrice == null) {
           return null;
        } else {
            return Pair.of(interQuantity, interPrice);
        }

    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Comp to(CompCreate compCreate);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CompCreated to(Comp comp);

    }


}
