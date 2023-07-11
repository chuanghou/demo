package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.common.Typed;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.basic.TypedEnums;
import com.stellariver.milky.demo.client.po.BidPO;
import com.stellariver.milky.demo.client.po.CentralizedBidPO;
import com.stellariver.milky.demo.client.po.RealtimeBidPO;
import com.stellariver.milky.demo.client.vo.OrderVO;
import com.stellariver.milky.demo.client.vo.UnitVO;
import com.stellariver.milky.demo.common.enums.Bid;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.Order;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UnitDOMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapping;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author houchuang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("demo")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitController {

    final DomainTunnel domainTunnel;
    final UnitDOMapper unitDOMapper;

    @PostMapping("centralizedBid")
    public Result<Void> centralizedBid(@RequestBody CentralizedBidPO centralizedBidPO, @RequestHeader("token") String token) {
        String userId = TokenUtils.getUserId(token);
        Unit unit = domainTunnel.getByAggregateId(Unit.class, centralizedBidPO.getTxGroup().getUnitId());
        BizEx.trueThrow(Kit.notEq(unit.getUserId(), userId), ErrorEnums.PARAM_FORMAT_WRONG.message("无权限操作"));

        UnitCommand.CentralizedBid centralizedBid = UnitCommand.CentralizedBid.builder()
                .txGroup(centralizedBidPO.getTxGroup())
                .bids(Collect.transfer(centralizedBidPO.getBidPOs(), Convertor.INST::to))
                .build();

        Map<Class<? extends Typed<?>>, Object> parameters = Collect.asMap(TypedEnums.USER_ID.class, userId);
        CommandBus.accept(centralizedBid, parameters);
        return Result.success();
    }

    @PostMapping("realtimeBid")
    public Result<Void> realtimeBid(@RequestBody RealtimeBidPO realtimeBidPO, @RequestHeader("token") String token) {
        String userId = TokenUtils.getUserId(token);
        Unit unit = domainTunnel.getByAggregateId(Unit.class, realtimeBidPO.getTxGroup().getUnitId());
        BizEx.trueThrow(Kit.notEq(unit.getUserId(), userId), ErrorEnums.PARAM_FORMAT_WRONG.message("无权限操作"));
        UnitCommand.RealtimeBid realtimeBid = UnitCommand.RealtimeBid.builder()
                .txGroup(realtimeBidPO.getTxGroup())
                .bid(Convertor.INST.to(realtimeBidPO.getBidPO()))
                .build();
        Map<Class<? extends Typed<?>>, Object> parameters = Collect.asMap(TypedEnums.USER_ID.class, userId);
        CommandBus.accept(realtimeBid, parameters);
        return Result.success();
    }


    @GetMapping("listUnits")
    public Result<List<UnitVO>> listOrders(@RequestHeader("token") String token) {
        String userId = TokenUtils.getUserId(token);
        LambdaQueryWrapper<UnitDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnitDO::getUserId, userId);
        List<UnitDO> unitDOs = unitDOMapper.selectList(wrapper);

        List<Unit> units = Collect.transfer(unitDOs, UnitDAOAdapter.Convertor.INST::to);

        List<UnitVO> unitVOs = units.stream().map(unit -> UnitVO.builder()
                .unitId(unit.getUnitId())
                .compId(unit.getCompId())
                .name(unit.getName())
                .orderVOs(Collect.transfer(unit.getOrders().values(), Convertor.INST::toOrderVO))
                .peakBids(unit.getCentralizedBids().get(TimeFrame.PEAK))
                .flatBids(unit.getCentralizedBids().get(TimeFrame.FLAT))
                .valleyBids(unit.getCentralizedBids().get(TimeFrame.VALLEY))
                .peakBuyBalance(unit.getBalanceQuantities().get(TimeFrame.PEAK).get(Direction.BUY))
                .peakSellBalance(unit.getBalanceQuantities().get(TimeFrame.PEAK).get(Direction.SELL))
                .flatBuyBalance(unit.getBalanceQuantities().get(TimeFrame.FLAT).get(Direction.BUY))
                .flatSellBalance(unit.getBalanceQuantities().get(TimeFrame.FLAT).get(Direction.SELL))
                .valleyBuyBalance(unit.getBalanceQuantities().get(TimeFrame.VALLEY).get(Direction.BUY))
                .valleySellBalance(unit.getBalanceQuantities().get(TimeFrame.VALLEY).get(Direction.SELL))
                .build()).collect(Collectors.toList());

        return Result.success(unitVOs);
    }



    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Bid to(BidPO bidPO);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        @Mapping(source = "txGroup", target = "txGroupVO")
        OrderVO toOrderVO(Order order);

    }

}
