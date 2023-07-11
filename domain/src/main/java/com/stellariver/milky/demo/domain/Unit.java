package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.wire.StaticWire;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.common.enums.*;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.domain.command.UnitEvent;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.command.ConstructorHandler;
import com.stellariver.milky.domain.support.command.MethodHandler;
import com.stellariver.milky.domain.support.context.Context;
import com.stellariver.milky.domain.support.dependency.Nulliable;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Builder;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
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

    String unitId;
    String compId;
    String name;
    Position position;
    UnitType unitType;
    String userId;

    Map<TimeFrame, Map<Direction, Double>> balanceQuantities;
    @lombok.Builder.Default
    Map<TimeFrame, List<Bid>> centralizedBids = new HashMap<>();
    @lombok.Builder.Default
    Map<String, Order> orders = new HashMap<>();

    @Nulliable(replacerSupplier = NullSupplier.class)
    Direction stageFourDirection;

    @StaticWire
    static private UniqueIdBuilder uniqueIdBuilder;

    @Override
    public String getAggregateId() {
        return unitId;
    }

    @ConstructorHandler
    public static Unit create(UnitCommand.UnitCreate unitCreate, Context context) {
        return Convertor.INST.to(unitCreate);
    }


    @MethodHandler
    public void handle(UnitCommand.CentralizedBid command, Context context) {
        Stage stage = context.getMetaData(TypedEnums.STAGE.class);
        if (stage == Stage.STAGE_ONE_RUNNING || stage == Stage.STAGE_THREE_RUNNING) {
            Direction direction = position.interProvincial();
            boolean b = unitType.generalDirection() == direction;
            BizEx.falseThrow(b, PARAM_FORMAT_WRONG.message("省间交易只能是送电省的机组和受电省的负荷"));
        }
        Direction direction = unitType.generalDirection();
        List<Bid> bids = command.getBids();
        bids.forEach(bid -> BizEx.trueThrow(bid.getDirection() != direction, PARAM_FORMAT_WRONG.message("买卖方向错误")));
        Double bidQuantity = bids.stream().map(Bid::getQuantity).reduce(0D, Double::sum);
        Double balanceQuantity = balanceQuantities.get(command.getTxGroup().getTimeFrame()).get(direction);
        BizEx.trueThrow(balanceQuantity > bidQuantity, PARAM_FORMAT_WRONG.message("余额不足"));
        centralizedBids.put(command.getTxGroup().getTimeFrame(), command.getBids());
        context.publish(UnitCommand.CentralizedBidden.builder().unitId(unitId).build());
    }

    @MethodHandler
    public void handle(UnitCommand.CentralizedTrigger command, Context context) {
        List<Order> centralizedOrders = new ArrayList<>();

        centralizedBids.forEach(((timeFrame, bids) -> {
            bids.forEach(bid -> {
                Order order = Order.builder()
                        .id(uniqueIdBuilder.get().toString())
                        .txGroup(TxGroup.builder().unitId(unitId).timeFrame(timeFrame).build())
                        .bid(bid)
                        .build();
                deduct(order);
                centralizedOrders.add(order);
            });
        }));

        UnitEvent.CentralizedBidden event = UnitEvent.CentralizedBidden.builder().unitId(unitId).orders(centralizedOrders).build();
        context.publish(event);
    }


    @MethodHandler
    public void handle(UnitCommand.RealtimeBid command, Context context) {

        Stage stage = context.getMetaData(TypedEnums.STAGE.class);
        if (stage == Stage.STAGE_FOUR_CLEARANCE) {
            if (stageFourDirection == null) {
                stageFourDirection = command.getBid().getDirection();
                if (stageFourDirection.opposite() == unitType.generalDirection()){
                    Double balance = balanceQuantities.get(command.getTxGroup().getTimeFrame()).get(stageFourDirection.opposite());
                    balanceQuantities.get(command.getTxGroup().getTimeFrame()).put(stageFourDirection, balance);
                } else {
                    throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
                }
            } else {
                boolean b = stageFourDirection != command.getBid().getDirection();
                BizEx.trueThrow(b, PARAM_FORMAT_WRONG.message("第四阶段只能发布同向的报价"));
            }
        } else {
            BizEx.trueThrow(unitType.generalDirection() != command.getBid().getDirection(), PARAM_FORMAT_WRONG.message("买卖方向错误"));
        }

        String orderId = uniqueIdBuilder.get().toString();
        Order order = Order.builder()
                .id(orderId)
                .txGroup(command.getTxGroup())
                .bid(command.getBid())
                .build();
        deduct(order);
        UnitEvent.RealtimeBidden event = UnitEvent.RealtimeBidden.builder().unitId(unitId).order(order).build();
        context.publish(event);
    }

    private void deduct(Order order) {
        orders.put(order.getId(), order);
        TimeFrame timeFrame = order.getTxGroup().getTimeFrame();
        Direction direction = order.getBid().getDirection();
        Double balance = balanceQuantities.get(timeFrame).get(direction);
        double newBalance = balance - order.getBid().getQuantity();
        BizEx.trueThrow(newBalance < 0, PARAM_FORMAT_WRONG.message("超过余额"));
        balanceQuantities.get(timeFrame).put(direction, newBalance);
    }


    @MethodHandler
    public void handle(UnitCommand.DealReport command, Context context) {
        Order order = orders.get(command.getBidId());
        order.getDeals().add(command.getDeal());
        UnitEvent.DealReported event = UnitEvent.DealReported.builder()
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
        Unit to(UnitCommand.UnitCreate unitCreate);

        @AfterMapping
        default void after(UnitCommand.UnitCreate unitCreate, @MappingTarget Unit unit) {
            Map<TimeFrame, Double> quantities = unitCreate.getQuantities();
            Map<TimeFrame, Map<Direction, Double>> balanceQuantities = new HashMap<>();
            Direction direction = unitCreate.getUnitType().generalDirection();
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
