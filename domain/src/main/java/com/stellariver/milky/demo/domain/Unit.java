package com.stellariver.milky.demo.domain;

import com.google.common.collect.Multimap;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.wire.StaticWire;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.TypedEnums;
import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.Order;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.domain.command.UnitEvent;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.command.ConstructorHandler;
import com.stellariver.milky.domain.support.command.MethodHandler;
import com.stellariver.milky.domain.support.context.Context;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Builder;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.stellariver.milky.common.base.ErrorEnumsBase.PARAM_FORMAT_WRONG;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Unit extends AggregateRoot {

    Long unitId;
    String userId;
    Long compId;
    Integer roundId;
    Province province;
    UnitType unitType;
    AbstractMetaUnit metaUnit;

    Map<TimeFrame, Direction> stageFourDirections = new HashMap<>();
    Map<TimeFrame, Map<Direction, Double>> balances = new HashMap<>();
    Map<MarketType, List<Bid>> centralizedBids = new HashMap<>();
    Map<Long, Bid> realtimeBids = new HashMap<>();

    @StaticWire
    static private UniqueIdBuilder uniqueIdBuilder;

    @Override
    public String getAggregateId() {
        return unitId.toString();
    }

    @ConstructorHandler
    public static Unit create(UnitCommand.Create create, Context context) {
        Unit unit = new Unit();
        unit.setUnitId(create.getUnitId());
        unit.setCompId(create.getCompId());
        unit.setRoundId(create.getRoundId());
        unit.setMetaUnit(create.getMetaUnit());
        unit.setBalances(create.getMetaUnit().getCapacity());
        unit.setUnitType(create.getMetaUnit().getUnitType());
        unit.setProvince(create.getMetaUnit().getProvince());
        context.publish(UnitEvent.Created.builder().unitId(unit.getUnitId()).unit(unit).build());
        return unit;
    }


    @MethodHandler
    public void handle(UnitCommand.CentralizedBid command, Context context) {
        MarketType marketType = context.getMetaData(TypedEnums.STAGE.class);

        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            boolean b = unitType.generalDirection() == province.interDirection();
            BizEx.falseThrow(b, PARAM_FORMAT_WRONG.message("省间交易只能是送电省的机组和受电省的负荷"));
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }

        Direction direction = unitType.generalDirection();
        List<Bid> bids = command.getBids();
        bids.forEach(bid -> BizEx.trueThrow(bid.getDirection() != direction, PARAM_FORMAT_WRONG.message("买卖方向错误")));

        Multimap<TimeFrame, Bid> groupBids = bids.stream().collect(Collect.listMultiMap(Bid::getTimeFrame));
        groupBids.keySet().forEach(timeFrame -> {
            Double balanceQuantity = balances.get(timeFrame).get(direction);
            Double totalBidQuantity = groupBids.get(timeFrame).stream().map(Bid::getQuantity).reduce(0D, Double::sum);
            BizEx.trueThrow(totalBidQuantity > balanceQuantity, PARAM_FORMAT_WRONG.message("余额不足"));
        });

        centralizedBids.put(marketType, command.getBids());
        UnitCommand.CentralizedBidden event = UnitCommand.CentralizedBidden.builder().unitId(unitId).bids(bids).build();
        context.publish(event);

    }

    @MethodHandler
    public void handle(UnitCommand.CentralizedTrigger command, Context context) {
        List<Bid> bids = centralizedBids.get(command.getMarketType());
        UnitEvent.CentralizedTriggered event = UnitEvent.CentralizedTriggered.builder()
                .unitId(unitId).marketType(command.getMarketType()).compId(compId).bids(bids).build();
        context.publish(event);
    }


    @MethodHandler
    public void handle(UnitCommand.RealtimeBid command, Context context) {

        MarketType marketType = context.getMetaData(TypedEnums.STAGE.class);
        Bid bid = command.getBid();
        if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL) {
            if (stageFourDirections.get(bid.getTimeFrame()) == null) {
                Direction direction = command.getBid().getDirection();
                if (direction.opposite() == unitType.generalDirection()){
                    Double balance = getBalances().get(bid.getTimeFrame()).get(unitType.generalDirection());
                    Double originalBalance = balances.get(bid.getTimeFrame()).remove(unitType.generalDirection());
                    balances.get(bid.getTimeFrame()).put(direction, originalBalance);
                }
            }
            Direction stageFourDirection = stageFourDirections.get(bid.getTimeFrame());
            boolean b = stageFourDirection != command.getBid().getDirection();
            BizEx.trueThrow(b, PARAM_FORMAT_WRONG.message("第四阶段只能发布同向的报价"));
        } else {
            BizEx.trueThrow(unitType.generalDirection() != command.getBid().getDirection(), PARAM_FORMAT_WRONG.message("买卖方向错误"));
        }

        Double balance = balances.get(bid.getTimeFrame()).get(bid.getDirection());
        BizEx.trueThrow(balance < bid.getQuantity(), PARAM_FORMAT_WRONG.message("超过余额"));
        realtimeBids.put(bid.getUnitId(), bid);
        UnitEvent.RealtimeBidden event = UnitEvent.RealtimeBidden.builder().unitId(unitId).bid(bid).build();
        context.publish(event);
    }

    @MethodHandler
    public void handle(UnitCommand.CentralizedDealReport command, Context context) {
        Map<Long, Deal> deals = command.getDeals();
        deals.forEach((bidId, deal) -> {

        });
        context.publish(event);
    }


    @MethodHandler
    public void handle(UnitCommand.RealtimeDealReport command, Context context) {
        Long bidId = command.getBidId();
        Bid bid = realtimeBids.get(bidId);
        bid.getDeals().add(command.getDeal());
        UnitEvent.RealtimeDealReported event = UnitEvent.RealtimeDealReported.builder()
                .unitId(unitId)
                .bidId(command.getBidId())
                .deal(command.getDeal())
                .build();
        context.publish(event);
    }

    @MethodHandler
    public void handle(UnitCommand.Cancel command, Context context) {
        String orderId = command.getOrderId();
        Order order = orders.get(orderId);
        UnitEvent.Cancelled event = UnitEvent.Cancelled.builder().unitId(unitId).order(order).build();
        context.publish(event);
    }

    @MethodHandler
    public void handle(UnitCommand.CancelReport command, Context context) {
        Order order = orders.get(command.getOrderId());
        order.setCancelled(command.getQuantity());
        TimeFrame timeFrame = command.getTxGroup().getTimeFrame();
        Direction direction = order.getBid().getDirection();
        Double balanceQuantity = balanceQuantities.get(timeFrame).get(direction);
        balanceQuantity += command.getQuantity();
        balanceQuantities.get(timeFrame).put(direction, balanceQuantity);
        UnitEvent.CancelledReported cancelled = UnitEvent.CancelledReported.builder().unitId(unitId).order(order).build();
        context.publish(cancelled);
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Unit to(UnitCommand.Create create);

        @AfterMapping
        default void after(UnitCommand.Create create, @MappingTarget Unit unit) {
            Map<TimeFrame, Double> quantities = create.getQuantities();
            Map<TimeFrame, Map<Direction, Double>> balanceQuantities = new HashMap<>();
            Direction direction = create.getUnitType().generalDirection();
            Direction oppositeDirection = direction.opposite();
            quantities.forEach((tf, quantity) -> {
                Map<Direction, Double> directionMap = new HashMap<>();
                directionMap.put(direction, quantities.get(tf));
                directionMap.put(oppositeDirection, 0D);
                balanceQuantities.put(tf, directionMap);
            });
            unit.setBalanceQuantities(balanceQuantities);
        }


    }

    static class NullSupplier implements Supplier<Object> {
        @Override
        public Direction get() {
            return Direction.UNKNOWN;
        }
    }

}
