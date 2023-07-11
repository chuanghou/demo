package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.wire.StaticWire;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.common.enums.*;
import com.stellariver.milky.demo.domain.command.CompClearStageOne;
import com.stellariver.milky.demo.domain.command.CompCreate;
import com.stellariver.milky.demo.domain.command.CompStep;
import com.stellariver.milky.demo.domain.event.CompClearStagedOne;
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
import org.apache.commons.lang3.tuple.Triple;
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

    List<Bid> stageOneCentralizedBids;
    List<Bid> stageThreeCentralizedBids;

    Map<TimeFrame, Pair<Double, Double>> limitations;
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


    @MethodHandler
    public void handle(CompClearStageOne compClearStageOne, Context context) {

        List<List<Bid>> collect = stageThreeCentralizedBids.stream().collect(Collect.select(
                bid -> bid.getDirection() == Direction.BUY,
                bid -> bid.getDirection() == Direction.SELL)
        );

        List<Bid> buyBids = collect.get(0);
        List<Bid> sellBids = collect.get(1);

        List<DealResult> dealResults0 = resolveDealResults(buyBids, sellBids, TimeFrame.PEAK);
        List<DealResult> dealResults1 = resolveDealResults(buyBids, sellBids, TimeFrame.FLAT);
        List<DealResult> dealResults2 = resolveDealResults(buyBids, sellBids, TimeFrame.VALLEY);

        List<DealResult> dealResults = Stream.of(dealResults0, dealResults1, dealResults2).flatMap(Collection::stream).collect(Collectors.toList());
        CompClearStagedOne event = CompClearStagedOne.builder().compId(compId).dealResults(dealResults).build();
        context.publish(event);
    }



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
    private List<DealResult> resolveDealResults(List<Bid> buyBids, List<Bid> sellBids, TimeFrame timeFrame) {

        Pair<Double, Double> limitation = limitations.get(timeFrame);

        buyBids = buyBids.stream().filter(bid -> bid.getTxGroup().getTimeFrame() == timeFrame).collect(Collectors.toList());
        sellBids = sellBids.stream().filter(bid -> bid.getTxGroup().getTimeFrame() == timeFrame).collect(Collectors.toList());

        buyBids = buyBids.stream().sorted(Comparator.comparing(Bid::getPrice).reversed()).collect(Collectors.toList());
        sellBids = sellBids.stream().sorted(Comparator.comparing(Bid::getPrice)).collect(Collectors.toList());

        List<PointLine> buyPointLines = buildPointLine(buyBids);
        List<PointLine> sellPointLines = buildPointLine(sellBids);

        Function<Double, Double> buyFunction = buildFx(buyPointLines);
        Function<Double, Double> sellFunction = buildFx(buyPointLines);

        Pair<Double, Double> interPoint;
        if (buyPointLines.get(0).getPrice() < sellPointLines.get(0).getPrice()) {
           interPoint = null;
        } else {
            interPoint = analyzeInterPoint(buyPointLines, sellPointLines);
            if (interPoint == null) {
                Double transferTotalQuantity = buyBids.stream().map(Bid::getQuantity).reduce(0D, Double::sum);
                Double receiveTotalQuantity = sellBids.stream().map(Bid::getQuantity).reduce(0D, Double::sum);
                double cumulateQuantity = Math.min(transferTotalQuantity, receiveTotalQuantity);
                Double buyPrice = buyFunction.apply(cumulateQuantity);
                Double sellPrice = sellFunction.apply(cumulateQuantity);
                interPoint = Pair.of(cumulateQuantity, (buyPrice + sellPrice) / 2);
            }
        }

        Double minTransfer = limitation.getLeft();
        Double maxTransfer = limitation.getLeft();

        Triple<Double, Double, Double> triple;

        if (interPoint == null) {
            triple = Triple.of(0D, null, null);
        } else if (interPoint.getLeft() <= minTransfer) {
            triple = Triple.of(interPoint.getLeft(), interPoint.getRight(), minTransfer - interPoint.getLeft());
        } else if (interPoint.getLeft() > minTransfer && interPoint.getLeft() <= maxTransfer) {
            triple =  Triple.of(interPoint.getLeft(), interPoint.getRight(), null);
        } else if (interPoint.getLeft() > maxTransfer) {
            double price = buyFunction.apply(maxTransfer) + sellFunction.apply(maxTransfer) / 2;
            triple = Triple.of(maxTransfer, price, null);
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }

        Double dealPrice;
        Double dealQuantity;

        List<DealResult> dealResults = new ArrayList<>();

        for (PointLine pointLine : buyPointLines) {
            if (pointLine.getCumulateQuantity() + pointLine.getQuantity() < triple.getLeft()) {
                dealQuantity = pointLine.getQuantity();
                dealPrice = triple.getMiddle();
            } else if (pointLine.getCumulateQuantity() < triple.getLeft()){
                dealQuantity = triple.getLeft() - pointLine.getCumulateQuantity();
                dealPrice = triple.getMiddle();
            } else {
                break;
            }

            Deal deal = Deal.builder()
                    .quantity(dealQuantity)
                    .price(dealPrice)
                    .build();

            DealResult dealResult = DealResult.builder()
                    .bidId(pointLine.getBidId())
                    .deal(deal)
                    .build();
            dealResults.add(dealResult);
        }


        for (PointLine pointLine : sellPointLines) {
            if (pointLine.getCumulateQuantity() + pointLine.getQuantity() < triple.getLeft()) {
                dealQuantity = pointLine.getQuantity();
                dealPrice = triple.getMiddle();
            } else if (pointLine.getCumulateQuantity() < triple.getLeft()){
                dealQuantity = triple.getLeft() - pointLine.getCumulateQuantity();
                dealPrice = triple.getMiddle();
            } else {
                break;
            }

            Deal deal = Deal.builder()
                    .quantity(dealQuantity)
                    .price(dealPrice)
                    .build();

            DealResult dealResult = DealResult.builder()
                    .bidId(pointLine.getBidId())
                    .deal(deal)
                    .build();
            dealResults.add(dealResult);
        }

        replenishMap.put(timeFrame, triple.getRight());

        return dealResults;

    }


    private Function<Double, Double> buildFx(List<PointLine> pointLines) {
        return x -> {
            for (PointLine pointLine : pointLines) {
                if (x <= pointLine.getCumulateQuantity() + pointLine.getQuantity()) {
                    return pointLine.getPrice();
                }
            }
            return null;
        };
    }

    static public List<PointLine> buildPointLine(List<Bid> bids) {
        List<PointLine> pointLines = new ArrayList<>();
        Double cumulateQuantity = 0D;
        for (Bid bid : bids) {
            PointLine pointLine = PointLine.builder()
                    .direction(bid.getDirection())
                    .cumulateQuantity(cumulateQuantity)
                    .price(bid.getPrice())
                    .quantity(bid.getQuantity())
                    .txGroup(bid.getTxGroup())
                    .build();
            pointLines.add(pointLine);
            cumulateQuantity += bid.getQuantity();
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
                        double p0 = ps.getCumulateQuantity() + ps.getQuantity();
                        double p1 = nextPs.getCumulateQuantity() + nextPs.getQuantity();
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
                        double p0 = ps.getCumulateQuantity() + ps.getQuantity();
                        double p1 = nextPs.getCumulateQuantity() + nextPs.getQuantity();
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
