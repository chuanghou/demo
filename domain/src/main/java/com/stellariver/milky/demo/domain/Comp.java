package com.stellariver.milky.demo.domain;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.BeanUtil;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.common.*;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.domain.event.CompEvent;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.domain.support.base.AggregateRoot;
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

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    Long compId;
    Integer agentTotal;
    Integer roundTotal;
    Integer roundId;
    Status.CompStatus compStatus;
    MarketType marketType;
    Status.MarketStatus marketStatus;
    PriceLimit priceLimit;
    Map<MarketType, Map<TimeFrame, GridLimit>> transLimit;
    List<Map<MarketType, Duration>> durations;
    List<Map<MarketType, Map<TimeFrame, Double>>> replenishes = new ArrayList<>();

    static Map<BidGroup, RealtimeBidProcessor> realtimeBidProcessors = new ConcurrentHashMap<>();

    @Override
    public String getAggregateId() {
        return compId.toString();
    }

    @ConstructorHandler
    static public Comp create(CompCommand.Create create, Context context) {
        Comp comp = new Comp();
        comp.setCompId(create.getCompId());
        comp.setRoundTotal(3);
        comp.setCompStatus(Status.CompStatus.END);
        comp.setRoundId(0);
        comp.setMarketType(MarketType.INTER_ANNUAL_PROVINCIAL);
        comp.setMarketStatus(Status.MarketStatus.CLOSE);
        comp.setPriceLimit(create.getPriceLimit());
        comp.setTransLimit(new HashMap<>());
        comp.setDurations(new ArrayList<>());
        comp.setReplenishes(new ArrayList<>());
        comp.setAgentTotal(create.getAgentTotal());
        context.publish(CompEvent.Created.builder().compId(comp.getCompId()).comp(comp).build());
        return comp;
    }


    @MethodHandler
    public void start(CompCommand.Start start, Context context) {
        BizEx.trueThrow(compStatus != Status.CompStatus.END, ErrorEnums.CONFIG_ERROR.message("需要初始化项目"));
        compStatus = Status.CompStatus.OPEN;
        CompEvent.Started started = CompEvent.Started.builder().compId(compId).build();
        context.publish(started);
    }

    @MethodHandler
    public void handle(CompCommand.Step command, Context context) {
        BizEx.trueThrow(compStatus != Status.CompStatus.OPEN, ErrorEnums.CONFIG_ERROR.message("本场未开放"));

        CompEvent.Stepped.SteppedBuilder<?, ?> builder = CompEvent.Stepped.builder()
                .compId(compId)
                .lastRoundId(roundId)
                .lastMarketType(marketType)
                .lastMarketStatus(marketStatus);

        Stage lastStage = Stage.builder().roundId(roundId).marketStatus(marketStatus).marketType(marketType).build();

        Stage targetStage = Stage.builder()
                .roundId(command.getTargetRoundId())
                .marketStatus(command.getTargetMarketStatus())
                .marketType(command.getTargetMarketType())
                .build();

        if (lastStage.next(roundTotal).equals(targetStage)) {
            roundId = targetStage.getRoundId();
            marketType = targetStage.getMarketType();
            marketStatus = targetStage.getMarketStatus();
        } else if (lastStage.equals(targetStage)) {
            return;
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }

        boolean b = Objects.equals(roundId, roundTotal) && marketType == MarketType.INTER_SPOT_PROVINCIAL;
        BizEx.trueThrow(b, ErrorEnums.CONFIG_ERROR.message("已经到了最后一轮"));

        CompEvent.Stepped stepped = builder
                .nextRoundId(roundId)
                .nextMarketType(marketType)
                .nextMarketStatus(marketStatus)
                .build();

        context.publish(stepped);

    }

    @MethodHandler
    public void handle(CompCommand.Close command, Context context) {
        compStatus = Status.CompStatus.END;
        CompEvent.End end = CompEvent.End.builder().compId(compId).build();
        context.publish(end);
    }


    @MethodHandler
    public void handle(CompCommand.CentralizedBid command, Context context) {
        compStatus = Status.CompStatus.END;
        CompEvent.End end = CompEvent.End.builder().compId(compId).build();
        context.publish(end);
    }

    @MethodHandler
    public void handle(CompCommand.Clear clear, Context context) {
        Tunnel tunnel = BeanUtil.getBean(Tunnel.class);
        List<Bid> bids = tunnel.getByCompId(compId).stream()
                .map(unit -> unit.getCentralizedBids().get(clear.getMarketType()))
                .flatMap(Collection::stream).collect(Collectors.toList());

        List<Bid> buyBids = bids.stream().filter(bid -> bid.getDirection() == Direction.BUY).collect(Collectors.toList());
        List<Bid> sellBids = bids.stream().filter(bid -> bid.getDirection() == Direction.SELL).collect(Collectors.toList());


        List<DealResult> dealResults0 = resolveDealResults(buyBids, sellBids, TimeFrame.PEAK);
        List<DealResult> dealResults1 = resolveDealResults(buyBids, sellBids, TimeFrame.FLAT);
        List<DealResult> dealResults2 = resolveDealResults(buyBids, sellBids, TimeFrame.VALLEY);

        List<DealResult> dealResults = Stream.of(dealResults0, dealResults1, dealResults2).flatMap(Collection::stream).collect(Collectors.toList());
        CompEvent.Cleared event = CompEvent.Cleared.builder().compId(compId).marketType(clear.getMarketType()).dealResults(dealResults).build();
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
    public List<DealResult> resolveDealResults(List<Bid> buyBids, List<Bid> sellBids, TimeFrame timeFrame) {


        buyBids = buyBids.stream().filter(bid -> bid.getTxGroup().getTimeFrame() == timeFrame).collect(Collectors.toList());
        sellBids = sellBids.stream().filter(bid -> bid.getTxGroup().getTimeFrame() == timeFrame).collect(Collectors.toList());

        ResolveResult resolveResult = resolveInterPoint(buyBids, sellBids);
        Pair<Double, Double> interPoint = resolveResult.getInterPoint();
        GridLimit gridLimit = transLimit.get(marketType).get(timeFrame);
        Triple<Double, Double, Double> triple;
        if (interPoint == null) {
            triple = Triple.of(0D, null, null);
        } else if (interPoint.getLeft() <= gridLimit.getLow()) {
            triple = Triple.of(interPoint.getLeft(), interPoint.getRight(), gridLimit.getLow() - interPoint.getLeft());
        } else if (interPoint.getLeft() > gridLimit.getLow() && interPoint.getLeft() <= gridLimit.getHigh()) {
            triple =  Triple.of(interPoint.getLeft(), interPoint.getRight(), null);
        } else if (interPoint.getLeft() > gridLimit.getHigh()) {
            Range<Double> buyRange = resolveResult.getBuyFunction().apply(gridLimit.getHigh());
            Range<Double> sellRange = resolveResult.getSellFunction().apply(gridLimit.getHigh());
            double buyPrice = (buyRange.lowerEndpoint()  + buyRange.upperEndpoint()) / 2;
            double sellPrice = (sellRange.lowerEndpoint()  + sellRange.upperEndpoint()) / 2;
            double price = (buyPrice + sellPrice) / 2;
            triple = Triple.of(gridLimit.getHigh(), price, null);
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }

        Double dealPrice;
        Double dealQuantity;

        List<DealResult> dealResults = new ArrayList<>();

        for (PointLine pointLine : resolveResult.getBuyPointLines()) {
            if (pointLine.getLeftX() + pointLine.getQuantity() < triple.getLeft()) {
                dealQuantity = pointLine.getQuantity();
                dealPrice = triple.getMiddle();
            } else if (pointLine.getLeftX() < triple.getLeft()){
                dealQuantity = triple.getLeft() - pointLine.getLeftX();
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


        for (PointLine pointLine : resolveResult.getSellPointLines()) {
            if (pointLine.getLeftX() + pointLine.getQuantity() < triple.getLeft()) {
                dealQuantity = pointLine.getQuantity();
                dealPrice = triple.getMiddle();
            } else if (pointLine.getLeftX() < triple.getLeft()){
                dealQuantity = triple.getLeft() - pointLine.getLeftX();
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

//        replenishMap.put(timeFrame, triple.getRight());

        return dealResults;

    }


    public ResolveResult resolveInterPoint(List<Bid> buyBids, List<Bid> sellBids) {

        buyBids = buyBids.stream().sorted(Comparator.comparing(Bid::getPrice).reversed()).collect(Collectors.toList());
        sellBids = sellBids.stream().sorted(Comparator.comparing(Bid::getPrice)).collect(Collectors.toList());


        if (buyBids.get(0).getPrice() < sellBids.get(0).getPrice()) {
            return null;
        }

        List<PointLine> buyPointLines = buildPointLine(buyBids);
        List<PointLine> sellPointLines = buildPointLine(sellBids);

        Function<Double, Range<Double>> buyFunction = buildFx(buyPointLines, Double.MAX_VALUE, 0D);
        Function<Double, Range<Double>> sellFunction = buildFx(sellPointLines, 0D, Double.MAX_VALUE);

        List<Double> collect0 = buyPointLines.stream().map(PointLine::getLeftX).collect(Collectors.toList());
        List<Double> collect1 = buyPointLines.stream().map(PointLine::getRightX).collect(Collectors.toList());
        List<Double> collect2 = sellPointLines.stream().map(PointLine::getLeftX).collect(Collectors.toList());
        List<Double> collect3 = sellPointLines.stream().map(PointLine::getRightX).collect(Collectors.toList());
        List<Double> xes = Stream.of(collect0, collect1, collect2, collect3)
                .flatMap(Collection::stream).distinct()
                .sorted(Double::compareTo)
                .collect(Collectors.toList());


        Pair<Double, Double> interPoint = null;

        for (Double x : xes) {
            Range<Double> buyRange = buyFunction.apply(x);
            Range<Double> sellRange = sellFunction.apply(x);
            if (buyRange == null || sellRange == null) {
                break;
            }
            if (!buyRange.isConnected(sellRange)) {
                continue;
            }
            Range<Double> intersection = buyRange.intersection(sellRange);
            if (Kit.eq(intersection.lowerEndpoint(), intersection.upperEndpoint())) {
                interPoint = Pair.of(x, intersection.lowerEndpoint());
                if (!Kit.eq(buyRange.lowerEndpoint(), sellRange.upperEndpoint())) {
                    break;
                }
            } else {
                double averagePrice = (intersection.lowerEndpoint() + intersection.upperEndpoint()) / 2;
                interPoint = Pair.of(x, averagePrice);
                break;
            }
        }

        return ResolveResult.builder()
                .buyFunction(buyFunction)
                .sellFunction(sellFunction)
                .buyPointLines(buyPointLines)
                .sellPointLines(sellPointLines)
                .interPoint(interPoint)
                .build();
    }

    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static public class ResolveResult {

        List<PointLine> buyPointLines;
        List<PointLine> sellPointLines;
        Function<Double, Range<Double>> buyFunction;
        Function<Double, Range<Double>> sellFunction;
        Pair<Double, Double> interPoint;

    }

    @SuppressWarnings("UnstableApiUsage")
    private Function<Double, Range<Double>> buildFx(List<PointLine> pointLines, Double startPrice, Double endPrice) {
        SysEx.trueThrow(Collect.isEmpty(pointLines), ErrorEnums.CONFIG_ERROR);
        RangeMap<Double, Range<Double>> rangeMap = TreeRangeMap.create();
        Double lastRightx = null;
        for (PointLine pointLine : pointLines) {
            double min = Math.min(startPrice, pointLine.getPrice());
            double max = Math.max(startPrice, pointLine.getPrice());
            Range<Double> value = Range.closed(min, max);
            rangeMap.put(Range.singleton(pointLine.getLeftX()), value);
            Range<Double> open = Range.open(pointLine.getLeftX(), pointLine.getRightX());
            rangeMap.put(open, Range.closed(pointLine.getPrice(), pointLine.getPrice()));
            startPrice = pointLine.getPrice();
            lastRightx = pointLine.getRightX();
        }
        
        double min = Math.min(startPrice, endPrice);
        double max = Math.max(startPrice, endPrice);
        
        rangeMap.put(Range.singleton(lastRightx), Range.closed(min, max));
        return rangeMap::get;
    }

    static public List<PointLine> buildPointLine(List<Bid> bids) {
        List<PointLine> pointLines = new ArrayList<>();
        Double cumulateQuantity = 0D;
        for (Bid bid : bids) {
            PointLine pointLine = PointLine.builder()
                    .direction(bid.getDirection())
                    .price(bid.getPrice())
                    .quantity(bid.getQuantity())
                    .txGroup(bid.getTxGroup())
                    .leftX(cumulateQuantity)
                    .rightX(cumulateQuantity + bid.getQuantity())
                    .width(bid.getQuantity())
                    .y(bid.getPrice())
                    .build();
            pointLines.add(pointLine);
            cumulateQuantity += bid.getQuantity();
        }
        return pointLines;
    }


    @MethodHandler
    public void handle(CompCommand.RealtimeBid command, Context context) {
        Bid bid = command.getBid();
        TimeFrame timeFrame = bid.getTimeFrame();
        BidGroup bidGroup = BidGroup.builder().compId(compId).timeFrame(timeFrame).build();
        RealtimeBidProcessor realtimeBidProcessor = realtimeBidProcessors.computeIfAbsent(bidGroup, bG -> new RealtimeBidProcessor());
        realtimeBidProcessor.post(bid);
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CompEvent.Created to(Comp comp);

    }


}
