package com.stellariver.milky.demo.domain;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.BeanUtil;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.wire.StaticWire;
import com.stellariver.milky.demo.basic.AgentConfig;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.common.*;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.domain.event.CompEvent;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.MethodHandler;
import com.stellariver.milky.domain.support.context.Context;
import com.stellariver.milky.domain.support.dependency.Milkywired;
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
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comp extends AggregateRoot {

    Integer compId;
    Integer roundNum;
    Status.CompStatus compStatus;
    Integer roundId;
    MarketType marketType;
    Status.MarketStatus marketStatus;

    Map<MarketType, Duration> durationMap;
    List<AgentConfig> agentConfigs;
    Map<TimeFrame, Pair<Double, Double>> limitations;
    Map<Pair<Integer, MarketType>, Map<TimeFrame, Double>> replenishMap = new HashMap<>();

    static Map<BidGroup, RealtimeBidProcessor> realtimeBidProcessors = new ConcurrentHashMap<>();

    @Override
    public String getAggregateId() {
        return compId.toString();
    }

    @MethodHandler
    public void reset(CompCommand.Reset reset, Context context) {
        compStatus = Status.CompStatus.CLOSE;
        Tunnel tunnel = BeanUtil.getBean(Tunnel.class);
        long loadCount = tunnel.loadLoadNumber();
        long generatorCount = tunnel.loadGeneratorNumber();
        long count = Math.min(loadCount, generatorCount) / 6 * 6;
        BizEx.trueThrow(count / 2 < reset.getAgentNumber(),  ErrorEnums.CONFIG_ERROR.message("数据库中机组或者负荷数量太少"));


        agentConfigs = new ArrayList<>();

        IntStream.range(1, reset.getAgentNumber() + 1).forEach(agentId -> IntStream.range(1, 4).forEach(roundId -> {
            Pair<Integer, Integer> allocateIds = allocate(roundId, agentId, reset.getAgentNumber(), (int) count);
            AgentConfig agentConfig = AgentConfig.builder()
                    .roundId(roundId)
                    .agentId(agentId)
                    .generatorId0(allocateIds.getLeft())
                    .generatorId1(allocateIds.getRight())
                    .loadId0(allocateIds.getLeft())
                    .loadId1(allocateIds.getRight())
                    .build();
            agentConfigs.add(agentConfig);
        }));


        context.publish(CompEvent.Reset.builder().compId(compId).build());
    }


    static Map<Integer, Pair<Integer, Integer>> roundOneMap = Collect.asMap(
            1, Pair.of(1, 6),
            2, Pair.of(2, 4),
            3, Pair.of(3, 5)
    );

    static Map<Integer, Pair<Integer, Integer>> roundTwoMap = Collect.asMap(
            1, Pair.of(2, 4),
            2, Pair.of(3, 5),
            3, Pair.of(1, 6)
    );

    static Map<Integer, Pair<Integer, Integer>> roundThreeMap = Collect.asMap(
            1, Pair.of(3, 5),
            2, Pair.of(1, 6),
            3, Pair.of(2, 4)
    );

    static Map<Integer, Map<Integer, Pair<Integer, Integer>>> alloacteMap = Collect.asMap(
            1, roundOneMap,
            2, roundTwoMap,
            3, roundThreeMap
    );

    private static Pair<Integer, Integer> allocate(Integer roundId, Integer userId, Integer userCount, Integer unitCount) {
        int groupMemberCount =  (userCount / 3) + (((userCount % 3) == 0) ? 0 : 1);
        Map<Integer, Pair<Integer, Integer>> integerPairMap = alloacteMap.get(roundId);
        int groupNumber = userId / groupMemberCount + (((userId % groupMemberCount) == 0) ? 0 : 1);
        Pair<Integer, Integer> pair = integerPairMap.get(groupNumber);
        int i = ((userId - 1) % groupMemberCount) + 1;
        int k = unitCount / 6;
        return Pair.of( (pair.getLeft() - 1) * k + i, (pair.getRight() - 1) * k + i);
    }

    @MethodHandler
    public void start(CompCommand.Start start, Context context) {
        BizEx.trueThrow(compStatus != Status.CompStatus.CLOSE, ErrorEnums.CONFIG_ERROR.message("需要初始化项目"));
        CompEvent.Started.StartedBuilder<?, ?> builder = CompEvent.Started.builder().compId(compId).lastCompStatus(compStatus);
        compStatus = Status.CompStatus.OPEN;
        roundId = 1;
        marketType = MarketType.INTER_ANNUAL_PROVINCIAL;
        marketStatus = Status.MarketStatus.OPEN;
        CompEvent.Started started = builder
                .nextCompStatus(compStatus)
                .roundId(roundId)
                .marketType(marketType)
                .marketStatus(marketStatus)
                .build();
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

        BizEx.trueThrow(marketStatus == Status.MarketStatus.OPEN, ErrorEnums.PARAM_FORMAT_WRONG);
        boolean b = Objects.equals(roundId, roundNum) && marketType == MarketType.INTER_SPOT_PROVINCIAL;
        BizEx.trueThrow(b, ErrorEnums.CONFIG_ERROR.message("已经到了最后一轮"));

        int nextDbCode = marketType.getDbCode() + 1;
        if (nextDbCode > MarketType.INTER_SPOT_PROVINCIAL.getDbCode()) {
            nextDbCode = MarketType.INTER_ANNUAL_PROVINCIAL.getDbCode();
            BizEx.trueThrow(Kit.eq(roundId, roundNum), ErrorEnums.CONFIG_ERROR.message("本场比赛已经结束!"));
            roundId += 1;
        }

        marketType = Kit.enumOfMightEx(MarketType::getDbCode, nextDbCode);
        CompEvent.Stepped stepped = builder.nextRoundId(roundId)
                .nextMarketType(marketType)
                .nextMarketStatus(marketStatus)
                .build();

        context.publish(stepped);

    }

    @MethodHandler
    public void handle(CompCommand.Close command, Context context) {
        BizEx.trueThrow(marketStatus != Status.MarketStatus.OPEN, ErrorEnums.CONFIG_ERROR.message("该市场不处于开放状态"));
        marketStatus = Status.MarketStatus.CLOSE;
        CompEvent.Closed closed = CompEvent.Closed.builder().compId(compId).roundId(roundId).marketType(marketType).build();
        context.publish(closed);
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
        Pair<Double, Double> limitation = limitations.get(timeFrame);


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
            Range<Double> buyRange = resolveResult.getBuyFunction().apply(maxTransfer);
            Range<Double> sellRange = resolveResult.getSellFunction().apply(maxTransfer);
            double buyPrice = (buyRange.lowerEndpoint()  + buyRange.upperEndpoint()) / 2;
            double sellPrice = (sellRange.lowerEndpoint()  + sellRange.upperEndpoint()) / 2;
            double price = (buyPrice + sellPrice) / 2;
            triple = Triple.of(maxTransfer, price, null);
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
        TimeFrame timeFrame = bid.getTxGroup().getTimeFrame();
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
