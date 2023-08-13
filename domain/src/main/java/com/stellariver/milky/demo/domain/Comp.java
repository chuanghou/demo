package com.stellariver.milky.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.*;
import com.stellariver.milky.common.base.BeanUtil;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.Clock;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.common.*;
import com.stellariver.milky.demo.common.enums.*;
import com.stellariver.milky.demo.domain.command.CompCommand;
import com.stellariver.milky.demo.domain.event.CompEvent;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.base.BaseDataObject;
import com.stellariver.milky.domain.support.command.ConstructorHandler;
import com.stellariver.milky.domain.support.command.MethodHandler;
import com.stellariver.milky.domain.support.context.Context;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
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
public class Comp extends AggregateRoot implements BaseDataObject<Long> {

    Long compId;
    Integer userTotal;
    Integer roundTotal;
    Integer roundId;
    Status.CompStatus compStatus;
    MarketType marketType;
    Status.MarketStatus marketStatus;
    PriceLimit priceLimit;
    Map<MarketType, Map<TimeFrame, GridLimit>> transLimit;
    Map<MarketType, Map<Status.MarketStatus, Duration>> durations;

    Map<Stage, Date> endTime;

    Boolean review;

    Integer paperNo = 1;

    @JsonIgnore
    List<Map<MarketType, Map<TimeFrame, Double>>> replenishes = new ArrayList<>();
    @JsonIgnore
    List<ListMultimap<MarketType, Bid>> centralizedBids = new ArrayList<>();

    List<Map<MarketType, Map<TimeFrame, CentralizedDeals>>> roundCentralizedDeals = new ArrayList<>();

    @JsonIgnore
    Map<RtProcessorKey, RealtimeBidProcessor> rtBidProcessors = new ConcurrentHashMap<>();

    Map<RtProcessorKey, RtCompVO> rtCompVOMap = new ConcurrentHashMap<>();

    @Override
    public String getAggregateId() {
        return compId.toString();
    }

    @ConstructorHandler
    static public Comp create(CompCommand.Create create, Context context) {
        Comp comp = new Comp();
        comp.setCompId(create.getCompId());
        comp.setRoundTotal(3);
        comp.setCompStatus(Status.CompStatus.INIT);
        comp.setRoundId(0);
        comp.setMarketType(MarketType.INTER_ANNUAL_PROVINCIAL);
        comp.setMarketStatus(Status.MarketStatus.CLOSE);
        comp.setPriceLimit(create.getPriceLimit());
        comp.setTransLimit(create.getTransLimit());
        comp.setDurations(create.getDurations());
        comp.setReplenishes(new ArrayList<>());
        comp.setUserTotal(create.getAgentTotal());

        IntStream.range(0, comp.getRoundTotal()).forEach(roundId -> {
            Map<MarketType, Map<TimeFrame, Double>> map = Collect.asMap(
                    MarketType.INTER_ANNUAL_PROVINCIAL, new HashMap<>(),
                    MarketType.INTER_MONTHLY_PROVINCIAL, new HashMap<>()
            );
            comp.getReplenishes().add(map);
        });
        IntStream.range(0, comp.getRoundTotal()).forEach(roundId -> comp.getCentralizedBids().add(ArrayListMultimap.create()));

        IntStream.range(0, comp.getRoundTotal()).forEach(roundId -> comp.getRoundCentralizedDeals().add(new HashMap<>()));

        context.publish(CompEvent.Created.builder().compId(comp.getCompId()).comp(comp).build());
        return comp;
    }


    @MethodHandler
    public void start(CompCommand.Start start, Context context) {
        BizEx.trueThrow(compStatus != Status.CompStatus.INIT, ErrorEnums.CONFIG_ERROR.message("需要初始化项目"));
        compStatus = Status.CompStatus.OPEN;
        marketStatus = Status.MarketStatus.OPEN;
        CompEvent.Started started = CompEvent.Started.builder().compId(compId).build();
        context.publish(started);
    }

    @MethodHandler
    public void update(CompCommand.TimeLine timeLine, Context context) {
        this.endTime = timeLine.getEndTime();
        context.publishPlaceHolderEvent(getAggregateId());
    }

    @MethodHandler
    public void update(CompCommand.Review review, Context context) {
        this.review = true;
        context.publishPlaceHolderEvent(getAggregateId());
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

        if (lastStage.laterThan(command.getNextStage()) || lastStage.equals(command.getNextStage())) {
            return;
        }  else if (lastStage.next().equals(command.getNextStage())){
            roundId = command.getNextStage().getRoundId();
            marketType = command.getNextStage().getMarketType();
            marketStatus = command.getNextStage().getMarketStatus();
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }

        boolean b = Objects.equals(roundId, roundTotal);
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
        centralizedBids.get(roundId).get(command.getMarketType()).addAll(command.getBids());
        CompEvent.CentralizedBidAccept event = CompEvent.CentralizedBidAccept.builder()
                .compId(compId)
                .roundId(roundId)
                .marketType(command.getMarketType())
                .bids(command.getBids())
                .build();
        context.publish(event);
    }

    @MethodHandler
    public void handle(CompCommand.Clear clear, Context context) {
        List<Bid> bids = centralizedBids.get(roundId).get(marketType);
        Map<TimeFrame, CentralizedDeals> marketTypeCentralizedDeals = new HashMap<>();
        for (TimeFrame timeFrame : TimeFrame.values()) {

            List<Bid> buyBids = bids.stream().filter(bid -> {
                boolean b0 = bid.getDirection() == Direction.BUY;
                boolean b1 = bid.getTimeFrame() == timeFrame;
                return b0 && b1;
            }).collect(Collectors.toList());
            List<Bid> sellBids = bids.stream().filter(bid -> {
                boolean b0 = bid.getDirection() == Direction.SELL;
                boolean b1 = bid.getTimeFrame() == timeFrame;
                return b0 && b1;
            }).collect(Collectors.toList());

            Clearance clearance = clear(buyBids, sellBids, timeFrame);

            Double dealQuantityTotal = clearance.getDeals().stream().map(Deal::getQuantity).reduce(0D, Double::sum) / 2;


            Double totalVolume = clearance.getDeals().stream().map(deal -> deal.getQuantity() * deal.getPrice()).reduce(0D, Double::sum);
            Double dealAveragePrice = Objects.equals(totalVolume, 0D) ? 0 : totalVolume/dealQuantityTotal;

            CentralizedDeals centralizedDeals = CentralizedDeals.builder()
                    .buyBidQuantityTotal(buyBids.stream().map(Bid::getQuantity).reduce(0D, Double::sum))
                    .sellBidQuantityTotal(sellBids.stream().map(Bid::getQuantity).reduce(0D, Double::sum))
                    .dealAveragePrice(dealAveragePrice)
                    .dealQuantityTotal(dealQuantityTotal)
                    .buyPointLines(clearance.getBuyPointLines())
                    .sellPointLines(clearance.getSellPointLines())
                    .interPoint(clearance.getInterPoint())
                    .deals(clearance.getDeals())
                    .build();

            marketTypeCentralizedDeals.put(timeFrame, centralizedDeals);
        }

        roundCentralizedDeals.get(roundId).put(marketType, marketTypeCentralizedDeals);
        CompEvent.Cleared event = CompEvent.Cleared.builder()
                .compId(compId)
                .marketType(marketType)
                .centralizedDealsMap(marketTypeCentralizedDeals)
                .replenishes(replenishes.get(roundId).get(marketType))
                .build();
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
    public Clearance clear(List<Bid> buyBids, List<Bid> sellBids, TimeFrame timeFrame) {

        if (Collect.isEmpty(buyBids) || Collect.isEmpty(sellBids)) {
            return Clearance.builder()
                    .deals(new ArrayList<>())
                    .interPoint(null)
                    .sellPointLines(buildPointLine(sellBids))
                    .buyPointLines(buildPointLine(buyBids))
                    .build();
        }

        ResolveResult resolveResult = resolveInterPoint(buyBids, sellBids);
        Pair<Double, Double> interPoint = resolveResult.getInterPoint();
        GridLimit gridLimit = transLimit.get(marketType).get(timeFrame);

        if (marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            CentralizedDeals centralizedDeals = roundCentralizedDeals.get(roundId).get(MarketType.INTER_ANNUAL_PROVINCIAL).get(timeFrame);
            Double dealQuantityTotal = centralizedDeals.getDealQuantityTotal();
            gridLimit = GridLimit.builder().low(gridLimit.getLow() - dealQuantityTotal).high(gridLimit.getHigh() - dealQuantityTotal).build();
        }

        Triple<Double, Double, Double> triple;  // left --> deal quantity, middle -> deal price, middle -> replenish
        if (interPoint == null) {
            triple = Triple.of(null, null, 0D);
        } else if (interPoint.getLeft() <= gridLimit.getLow()) {
            triple = Triple.of(interPoint.getLeft(), interPoint.getRight(), gridLimit.getLow() - interPoint.getLeft());
        } else if (interPoint.getLeft() > gridLimit.getLow() && interPoint.getLeft() <= gridLimit.getHigh()) {
            triple =  Triple.of(interPoint.getLeft(), interPoint.getRight(), 0D);
        } else if (interPoint.getLeft() > gridLimit.getHigh()) {
            Range<Double> buyRange = resolveResult.getBuyFunction().apply(gridLimit.getHigh());
            Range<Double> sellRange = resolveResult.getSellFunction().apply(gridLimit.getHigh());
            double buyPrice = (buyRange.lowerEndpoint()  + buyRange.upperEndpoint()) / 2;
            double sellPrice = (sellRange.lowerEndpoint()  + sellRange.upperEndpoint()) / 2;
            double price = (buyPrice + sellPrice) / 2;
            triple = Triple.of(gridLimit.getHigh(), price, 0D);
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }

        List<Deal> buyDeals = resolveDeals(resolveResult.getBuyPointLines(), triple);
        List<Deal> sellDeals = resolveDeals(resolveResult.getSellPointLines(), triple);
        Map<TimeFrame, Double> replenishMap = replenishes.get(roundId).get(marketType);
        replenishMap.put(timeFrame, triple.getRight());
        List<Deal> deals = Stream.of(buyDeals, sellDeals).flatMap(Collection::stream).collect(Collectors.toList());
        return  Clearance.builder().deals(deals)
                .buyPointLines(resolveResult.getBuyPointLines())
                .sellPointLines(resolveResult.getSellPointLines())
                .interPoint(resolveResult.getInterPoint())
                .build();

    }

    private List<Deal> resolveDeals(List<PointLine> pointLines, Triple<Double, Double, Double> triple) {
        List<Deal> deals = new ArrayList<>();
        if (triple.getLeft() == null) {
            return deals;
        }
        UniqueIdBuilder uniqueIdBuilder = BeanUtil.getBean(UniqueIdBuilder.class);
        int lastPointLineIndex = 0;
        for (int i = 0; i < pointLines.size(); i++) {
            PointLine pointLine = pointLines.get(i);
            boolean b = pointLine.getLeftX() < triple.getLeft();
            if (b) {
                lastPointLineIndex = i;
            } else {
                break;
            }
        }

        double totalDealQuantity = 0;
        double totalQuantity = 0;
        double lastPartQuantity = triple.getLeft() - pointLines.get(lastPointLineIndex).getLeftX();
        totalDealQuantity += lastPartQuantity;
        totalQuantity += pointLines.get(lastPointLineIndex).getQuantity();
        int exclusivePointLineIndex = lastPointLineIndex - 1;

        for (; exclusivePointLineIndex >= 0; exclusivePointLineIndex--) {
            boolean b = Kit.notEq(pointLines.get(exclusivePointLineIndex).getPrice(), triple.getMiddle());
            if (b) {
                break;
            }
        }

        for (int i = exclusivePointLineIndex + 1; i < lastPointLineIndex; i++) {
            PointLine pointLine = pointLines.get(i);
            totalDealQuantity += pointLine.getQuantity();
            totalQuantity += pointLine.getQuantity();
        }


        for (int i = exclusivePointLineIndex + 1; i < lastPointLineIndex; i++) {
            PointLine pointLine = pointLines.get(i);
            Deal deal = Deal.builder().dealId(uniqueIdBuilder.get())
                    .bidId(pointLine.getBidId())
                    .unitId(pointLine.getUnitId())
                    .quantity(pointLine.getQuantity() * totalDealQuantity / totalQuantity)
                    .price(triple.getMiddle())
                    .date(Clock.now())
                    .build();
            deals.add(deal);
        }

        PointLine lastPointLine = pointLines.get(lastPointLineIndex);
        Deal deal = Deal.builder().dealId(uniqueIdBuilder.get())
                .bidId(lastPointLine.getBidId())
                .unitId(lastPointLine.getUnitId())
                .quantity(lastPointLine.getQuantity() * totalDealQuantity / totalQuantity)
                .price(triple.getMiddle())
                .date(Clock.now())
                .build();
        deals.add(deal);


        for (int i = 0; i <= exclusivePointLineIndex; i++) {
            PointLine pointLine = pointLines.get(i);
            deal = Deal.builder().dealId(uniqueIdBuilder.get())
                    .bidId(pointLine.getBidId())
                    .unitId(pointLine.getUnitId())
                    .quantity(pointLine.getQuantity())
                    .price(triple.getMiddle())
                    .date(Clock.now())
                    .build();
            deals.add(deal);
        }
        return deals;
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
        List<Double> xes = Stream.of(collect0, collect1, collect2, collect3).flatMap(Collection::stream).distinct()
                .sorted(Double::compareTo).collect(Collectors.toList());


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

    @Override
    public Long getPrimaryId() {
        return compId;
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
        @Nullable
        Pair<Double, Double> interPoint;

    }

    @SuppressWarnings("UnstableApiUsage")
    private Function<Double, Range<Double>> buildFx(List<PointLine> pointLines, Double startPrice, Double endPrice) {
        if (pointLines.isEmpty()) {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }
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
                    .bidId(bid.getBidId())
                    .unitId(bid.getUnitId())
                    .direction(bid.getDirection())
                    .price(bid.getPrice())
                    .quantity(bid.getQuantity())
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
    public void handle(CompCommand.RtNewBid command, Context context) {
        NewBid newBid = command.getNewBid();
        Comp comp = BeanUtil.getBean(Tunnel.class).runningComp();
        RtProcessorKey processorKey = RtProcessorKey.builder().province(newBid.getProvince())
                .timeFrame(newBid.getTimeFrame())
                .marketType(comp.getMarketType())
                .roundId(comp.getRoundId())
                .build();

        RealtimeBidProcessor realtimeBidProcessor = rtBidProcessors.computeIfAbsent(processorKey, RealtimeBidProcessor::new);
        realtimeBidProcessor.post(newBid);
    }

    @MethodHandler
    public void handle(CompCommand.RtCancelBid command, Context context) {
        Comp comp = BeanUtil.getBean(Tunnel.class).runningComp();
        RtProcessorKey processorKey = RtProcessorKey.builder().province(command.getProvince())
                .timeFrame(command.getTimeFrame())
                .marketType(comp.getMarketType())
                .roundId(comp.getRoundId())
                .build();
        RealtimeBidProcessor realtimeBidProcessor = rtBidProcessors.get(processorKey);
        CancelBid cancelBid = new CancelBid(command.getBidId(), command.getBidDirection());
        realtimeBidProcessor.post(cancelBid);
        context.publishPlaceHolderEvent(getAggregateId());
    }

    @MethodHandler
    public void handle(CompCommand.RtMarketClose command, Context context) {
        rtBidProcessors.values().forEach(RealtimeBidProcessor::close);
    }



}
