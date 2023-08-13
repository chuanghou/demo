package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.*;
import com.stellariver.milky.common.tool.common.Clock;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.common.Typed;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.basic.TypedEnums;
import com.stellariver.milky.demo.client.vo.BidVO;
import com.stellariver.milky.demo.client.vo.DealVO;
import com.stellariver.milky.demo.common.Deal;
import com.stellariver.milky.demo.common.enums.*;
import com.stellariver.milky.demo.client.po.BidPO;
import com.stellariver.milky.demo.client.po.CentralizedBidPO;
import com.stellariver.milky.demo.client.po.RealtimeCancelBidPO;
import com.stellariver.milky.demo.client.po.RealtimeNewBidPO;
import com.stellariver.milky.demo.client.vo.BalanceVO;
import com.stellariver.milky.demo.client.vo.UnitVO;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.PriceLimit;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UnitDOMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author houchuang
 */
@RestController
@RequiredArgsConstructor
    @RequestMapping("unit")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitController {

    final DomainTunnel domainTunnel;
    final UnitDOMapper unitDOMapper;
    final Tunnel tunnel;

    @GetMapping("listUnits")
    public Result<List<Unit>> listUnits(@RequestHeader String token) {
        Comp comp = tunnel.runningComp();
        BizEx.nullThrow(comp, ErrorEnums.PARAM_FORMAT_WRONG.message("当前无运行竞赛"));
        String userId = TokenUtils.getUserId(token);
        LambdaQueryWrapper<UnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UnitDO::getCompId, comp.getCompId());
        queryWrapper.eq(UnitDO::getUserId, userId);
        queryWrapper.eq(UnitDO::getRoundId, comp.getRoundId());
        List<UnitDO> unitDOS = unitDOMapper.selectList(queryWrapper);
        List<Unit> units = Collect.transfer(unitDOS, UnitDAOAdapter.Convertor.INST::to);
        return Result.success(units);
    }


    @GetMapping("listUnitTransactions")
    public Result<List<UnitVO>> listUnitVOs(@RequestParam String marketTypeValue,
                                          @RequestHeader String token) {
        Comp comp = tunnel.runningComp();
        String userId = TokenUtils.getUserId(token);
        LambdaQueryWrapper<UnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UnitDO::getCompId, comp.getCompId());
        if (!Kit.eq(comp.getReview(), true)) {
            queryWrapper.eq(UnitDO::getUserId, Integer.parseInt(userId));
        }
        queryWrapper.eq(UnitDO::getRoundId, comp.getRoundId());
        List<Unit> units = Collect.transfer(unitDOMapper.selectList(queryWrapper), UnitDAOAdapter.Convertor.INST::to);
        List<UnitVO> unitVOs = Arrays.stream(TimeFrame.values()).map(t -> {
            return units.stream().map(u -> to(u, MarketType.valueOf(marketTypeValue), t)).collect(Collectors.toList());
        }).flatMap(Collection::stream).collect(Collectors.toList());
        return Result.success(unitVOs);
    }

    private UnitVO to(Unit unit, MarketType marketType, TimeFrame timeFrame) {

        BalanceVO balanceVO = getBalanceVO(unit, timeFrame, marketType);

        List<Bid> bids = unit.getBids().values().stream().filter(bid -> {
            boolean b0 = bid.getTimeFrame() == timeFrame;
            boolean b1 = bid.getMarketType() == marketType;
            return b0 && b1;
        }).collect(Collectors.toList());

        List<BidVO> bidVOs = bids.stream().map(bid -> {
            Double sum = bid.getDeals().stream().map(Deal::getQuantity).reduce(0D, Double::sum);
            return BidVO.builder().quantity(bid.getQuantity())
                    .price(bid.getPrice())
                    .cancelable(bid.getBidStatus() == BidStatus.NEW_DECELERATED || bid.getBidStatus() == BidStatus.PART_DEAL)
                    .notDeal(bid.getQuantity() - sum)
                    .dealVOs(toDealVOs(bid))
                    .build();
        }).collect(Collectors.toList());

        return UnitVO.builder().unitId(unit.getUnitId())
                .name(unit.getMetaUnit().getName())
                .timeFrame(timeFrame)
                .province(unit.getMetaUnit().getProvince())
                .unitType(unit.getMetaUnit().getUnitType())
                .bidVOs(bidVOs)
                .balanceVO(balanceVO)
                .build();
    }

    private List<DealVO> toDealVOs(Bid bid) {
        List<DealVO> dealVOS = new ArrayList<>();
        for (Deal deal: bid.getDeals()) {
            DealVO dealVO = DealVO.builder().status("成交")
                    .date(deal.getDate())
                    .price(deal.getPrice())
                    .quantity(deal.getQuantity())
                    .build();
            dealVOS.add(dealVO);
        }

        Double reduce = bid.getDeals().stream().map(Deal::getQuantity).reduce(0D, Double::sum);

        if (bid.getBidStatus() == BidStatus.CANCELLED) {
            DealVO dealVO = DealVO.builder().status("已撤")
                    .price(bid.getPrice())
                    .date(bid.getCancelledDate())
                    .quantity(bid.getQuantity() - reduce)
                    .build();
            dealVOS.add(dealVO);
        }
        return dealVOS;
    }

    private BalanceVO getBalanceVO(Unit unit, TimeFrame timeFrame, MarketType marketType) {
        List<Direction> directions = new ArrayList<>();
        BalanceVO balanceVO0;
        Direction generalDirection = unit.getMetaUnit().getUnitType().generalDirection();

        directions.add(generalDirection);
        if (marketType == MarketType.INTRA_MONTHLY_PROVINCIAL) {
            directions.add(generalDirection.opposite());
        }


        Double dealed = unit.getBids().values().stream()
                .filter(bid -> bid.getDirection() == generalDirection)
                .filter(bid -> bid.getTimeFrame() == timeFrame)
                .map(Bid::getDeals)
                .flatMap(Collection::stream).map(Deal::getQuantity).reduce(0D, Double::sum);

        Double reverseDealed = unit.getBids().values().stream()
                .filter(bid -> bid.getDirection() == generalDirection.opposite())
                .filter(bid -> bid.getTimeFrame() == timeFrame)
                .map(Bid::getDeals)
                .flatMap(Collection::stream).map(Deal::getQuantity).reduce(0D, Double::sum);

        dealed = dealed - reverseDealed;

        Double onMatching0 = unit.getBids().values().stream()
                .filter(bid -> bid.getDirection() == generalDirection)
                .filter(bid -> bid.getTimeFrame() == timeFrame)
                .filter(bid -> bid.getBidStatus() != BidStatus.CANCELLED)
                .map(bid -> bid.getQuantity() - bid.getDeals().stream().map(Deal::getQuantity).reduce(0D, Double::sum))
                .reduce(0D, Double::sum);

        Double onMatching1 = unit.getBids().values().stream()
                .filter(bid -> bid.getDirection() == generalDirection.opposite())
                .filter(bid -> bid.getTimeFrame() == timeFrame)
                .filter(bid -> bid.getBidStatus() != BidStatus.CANCELLED)
                .map(bid -> bid.getQuantity() - bid.getDeals().stream().map(Deal::getQuantity).reduce(0D, Double::sum))
                .reduce(0D, Double::sum);


        return BalanceVO.builder()
                .directions(directions)
                .capacity(unit.getMetaUnit().getCapacity().get(timeFrame).get(generalDirection))
                .dealed(dealed)
                .onMatching(Math.max(onMatching0, onMatching1))
                .build();
    }

    private void checkPrice(UnitType unitType, PriceLimit priceLimit, Double price) {
        if (unitType.equals(UnitType.LOAD)) {
            priceLimit.getLoadPriceLimit().check(price);
        } else if (unitType.equals(UnitType.GENERATOR)) {
            priceLimit.getGeneratorPriceLimit().check(price);
        } else {
            throw new SysEx(ErrorEnums.UNREACHABLE_CODE);
        }
    }


    @PostMapping("centralizedBid")
    public Result<Void> centralizedBid(@RequestBody CentralizedBidPO centralizedBidPO, @RequestHeader("token") String token) {

        Integer userId = Integer.parseInt(TokenUtils.getUserId(token));
        Unit unit = domainTunnel.getByAggregateId(Unit.class, centralizedBidPO.getUnitId().toString());
        BizEx.trueThrow(Kit.notEq(unit.getUserId(), userId), ErrorEnums.PARAM_FORMAT_WRONG.message("无权限操作"));

        Comp comp = tunnel.runningComp();
        PriceLimit priceLimit = comp.getPriceLimit();

        List<Bid> bids = Collect.transfer(centralizedBidPO.getBids(), Convertor.INST::to);
        bids.forEach(bid -> {
            bid.setUnitId(centralizedBidPO.getUnitId());
            bid.setProvince(unit.getMetaUnit().getProvince());
            checkPrice(unit.getMetaUnit().getUnitType(), priceLimit, bid.getPrice());
        });

        Map<Class<? extends Typed<?>>, Object> parameters = Collect.asMap(TypedEnums.STAGE.class, comp.getMarketType());
        UnitCommand.CentralizedBid command = UnitCommand.CentralizedBid.builder().unitId(centralizedBidPO.getUnitId()).bids(bids).build();
        CommandBus.accept(command, parameters);
        return Result.success();
    }

    @PostMapping("realtimeNewBid")
    public Result<Void> realtimeNewBid(@RequestBody RealtimeNewBidPO realtimeNewBidPO, @RequestHeader("token") String token) {
        Integer userId = Integer.parseInt(TokenUtils.getUserId(token));
        Unit unit = domainTunnel.getByAggregateId(Unit.class, realtimeNewBidPO.getUnitId().toString());
        BizEx.trueThrow(Kit.notEq(unit.getUserId(), userId), ErrorEnums.PARAM_FORMAT_WRONG.message("无权限操作"));
        Bid bid = Convertor.INST.to(realtimeNewBidPO.getBid());
        bid.setUnitId(realtimeNewBidPO.getUnitId());
        bid.setProvince(unit.getMetaUnit().getProvince());
        Comp comp = tunnel.runningComp();
        checkPrice(unit.getMetaUnit().getUnitType(), comp.getPriceLimit(), bid.getPrice());
        Map<Class<? extends Typed<?>>, Object> parameters = Collect.asMap(TypedEnums.STAGE.class, comp.getMarketType());
        UnitCommand.RtNewBidDeclare realtimeBid = UnitCommand.RtNewBidDeclare.builder().unitId(unit.getUnitId()).bid(bid).build();
        CommandBus.accept(realtimeBid, parameters);
        return Result.success();
    }

    @PostMapping("realtimeCancelBid")
    public Result<Void> realtimeCancelBid(@RequestBody RealtimeCancelBidPO realtimeCancelBidPO, @RequestHeader("token") String token) {
        Integer userId = Integer.parseInt(TokenUtils.getUserId(token));
        Unit unit = domainTunnel.getByAggregateId(Unit.class, realtimeCancelBidPO.getUnitId().toString());
        BizEx.trueThrow(Kit.notEq(unit.getUserId(), userId), ErrorEnums.PARAM_FORMAT_WRONG.message("无权限操作"));
        Comp comp = tunnel.runningComp();
        Map<Class<? extends Typed<?>>, Object> parameters = Collect.asMap(TypedEnums.STAGE.class, comp.getMarketType());
        UnitCommand.RtCancelBidDeclare cancelBidDeclare = UnitCommand.RtCancelBidDeclare
                .builder().unitId(unit.getUnitId()).bidId(realtimeCancelBidPO.getBidId()).build();
        CommandBus.accept(cancelBidDeclare, parameters);
        return Result.success();
    }

    @GetMapping("listRealtimeBids")
    public Result<List<Bid>> listRealtimeBids() {
        return null;
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Bid to(BidPO bidPO);


        @AfterMapping
        @SuppressWarnings("unused")
        default void after(BidPO bidPO, @MappingTarget Bid bid) {
            bid.setBidId(BeanUtil.getBean(UniqueIdBuilder.class).get());
            bid.setDate(Clock.now());
            bid.setMarketType(BeanUtil.getBean(Tunnel.class).runningComp().getMarketType());
        }

    }

    public static void main(String[] args) {
        System.out.println(TokenUtils.sign("0"));
    }

}
