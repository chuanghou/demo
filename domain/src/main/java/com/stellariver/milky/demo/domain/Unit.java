package com.stellariver.milky.demo.domain;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
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
import com.stellariver.milky.demo.common.enums.BidStatus;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stellariver.milky.common.base.ErrorEnumsBase.PARAM_FORMAT_WRONG;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Unit extends AggregateRoot {

    Long unitId;
    Integer userId;
    Long compId;
    Integer roundId;

    AbstractMetaUnit metaUnit;

    Map<Long, Bid> bids = new HashMap<>();

    ListMultimap<MarketType, Bid> centralizedBids = ArrayListMultimap.create();

    Map<TimeFrame, Direction> stageFourDirections = new HashMap<>();
    Map<TimeFrame, Map<Direction, Double>> balances = new HashMap<>();

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
        unit.setUserId(create.getUserId());
        unit.setCompId(create.getCompId());
        unit.setRoundId(create.getRoundId());
        unit.setMetaUnit(create.getMetaUnit());
        unit.setBalances(create.getMetaUnit().getCapacity());
        context.publish(UnitEvent.Created.builder().unitId(unit.getUnitId()).unit(unit).build());
        return unit;
    }


    @MethodHandler
    public void handle(UnitCommand.CentralizedBid command, Context context) {
        MarketType marketType = context.getMetaData(TypedEnums.STAGE.class);

        if (marketType == MarketType.INTER_ANNUAL_PROVINCIAL || marketType == MarketType.INTER_MONTHLY_PROVINCIAL) {
            UnitType unitType = metaUnit.getUnitType();
            Province province = metaUnit.getProvince();
            boolean b = unitType.generalDirection() == province.interDirection();
            BizEx.falseThrow(b, PARAM_FORMAT_WRONG.message("省间交易只能是送电省的机组和受电省的负荷"));
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }
        UnitType unitType = metaUnit.getUnitType();
        Direction direction = unitType.generalDirection();
        List<Bid> bids = command.getBids();
        bids.forEach(bid -> BizEx.trueThrow(bid.getDirection() != direction, PARAM_FORMAT_WRONG.message("买卖方向错误")));

        Multimap<TimeFrame, Bid> groupBids = bids.stream().collect(Collect.listMultiMap(Bid::getTimeFrame));
        groupBids.keySet().forEach(timeFrame -> {
            Double balanceQuantity = balances.get(timeFrame).get(direction);
            Double totalBidQuantity = groupBids.get(timeFrame).stream().map(Bid::getQuantity).reduce(0D, Double::sum);
            BizEx.trueThrow(totalBidQuantity > balanceQuantity, PARAM_FORMAT_WRONG.message("余额不足"));
        });


        centralizedBids.putAll(marketType, command.getBids());
        UnitCommand.CentralizedBidden event = UnitCommand.CentralizedBidden.builder().unitId(unitId).bids(bids).build();
        context.publish(event);

    }

    @MethodHandler
    public void handle(UnitCommand.CentralizedTrigger command, Context context) {
        List<Bid> marketTypeBids = centralizedBids.get(command.getMarketType());
        marketTypeBids.forEach(bid -> {
            bid.setBidStatus(BidStatus.NEW_DECELERATED);
            bids.put(bid.getBidId(), bid);
        });
        UnitEvent.CentralizedTriggered event = UnitEvent.CentralizedTriggered.builder()
                .unitId(unitId).marketType(command.getMarketType()).compId(compId).bids(marketTypeBids).build();
        context.publish(event);
    }




    @MethodHandler
    public void handle(UnitCommand.DealReport command, Context context) {
        List<Deal> deals = command.getDeals();
        deals.forEach(deal -> {
            Bid bid = bids.get(deal.getBidId());
            bid.getDeals().add(deal);
            Double sumQuantity = bid.getDeals().stream().map(Deal::getQuantity).reduce(0D, Double::sum);
            bid.setBidStatus(sumQuantity < bid.getQuantity() ? BidStatus.PART_DEAL : BidStatus.COMPLETE_DEAL);
        });
        context.publishPlaceHolderEvent(getAggregateId());
    }

    @MethodHandler
    public void handle(UnitCommand.RtNewBidDeclare command, Context context) {

        MarketType marketType = context.getMetaData(TypedEnums.STAGE.class);
        Bid bid = command.getBid();
        if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL) {
            if (stageFourDirections.get(bid.getTimeFrame()) == null) {
                Direction direction = command.getBid().getDirection();
                UnitType unitType = metaUnit.getUnitType();
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
            UnitType unitType = metaUnit.getUnitType();
            BizEx.trueThrow(unitType.generalDirection() != command.getBid().getDirection(), PARAM_FORMAT_WRONG.message("买卖方向错误"));
        }

        Double balance = balances.get(bid.getTimeFrame()).get(bid.getDirection());
        BizEx.trueThrow(balance < bid.getQuantity(), PARAM_FORMAT_WRONG.message("超过余额"));
        bid.setBidStatus(BidStatus.NEW_DECELERATED);
        bids.put(bid.getUnitId(), bid);
        UnitEvent.RtBidDeclared event = UnitEvent.RtBidDeclared.builder().unitId(unitId).bid(bid).build();
        context.publish(event);
    }

    @MethodHandler
    public void handle(UnitCommand.RtCancelBidDeclare command, Context context) {
        Long bidId = command.getBidId();
        Bid bid = bids.get(bidId);
        BizEx.trueThrow(BidStatus.COMPLETE_DEAL == bid.getBidStatus(), PARAM_FORMAT_WRONG.message("已经成交不可撤单！"));
        BizEx.trueThrow(BidStatus.CANCEL_DECELERATED == bid.getBidStatus(), PARAM_FORMAT_WRONG.message("撤单指令已报，请不要重复操作！"));
        BizEx.trueThrow(BidStatus.CANCELLED == bid.getBidStatus(), PARAM_FORMAT_WRONG.message("撤单指令已经完成"));
        bid.setBidStatus(BidStatus.CANCEL_DECELERATED);
        UnitEvent.RtCancelBidDeclared event = UnitEvent.RtCancelBidDeclared.builder().unitId(unitId).compId(compId).bid(bid).build();
        context.publish(event);
    }

    @MethodHandler
    public void handle(UnitCommand.RtBidCancelled command, Context context) {
        Bid bid = bids.get(command.getBidId());
        bid.setBidStatus(BidStatus.CANCELLED);
        Map<Direction, Double> timeFrameBalance = balances.get(bid.getTimeFrame());
        Double balance = timeFrameBalance.get(bid.getDirection());
        timeFrameBalance.put(bid.getDirection(), balance + command.getRemainder());
        context.publishPlaceHolderEvent(getAggregateId());
    }


}
