package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.BeanUtil;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.Clock;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.common.Typed;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.basic.TypedEnums;
import com.stellariver.milky.demo.client.po.BidPO;
import com.stellariver.milky.demo.client.po.CentralizedBidPO;
import com.stellariver.milky.demo.client.po.RealtimeCancelBidPO;
import com.stellariver.milky.demo.client.po.RealtimeNewBidPO;
import com.stellariver.milky.demo.common.Bid;
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

import java.util.List;
import java.util.Map;

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
    public Result<List<Unit>> listUnits(@RequestParam Long compId, @RequestHeader String token) {
        Comp comp = domainTunnel.getByAggregateId(Comp.class, compId.toString());
        String userId = TokenUtils.getUserId(token);
        LambdaQueryWrapper<UnitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UnitDO::getCompId, comp.getCompId());
        queryWrapper.eq(UnitDO::getUserId, userId);
        queryWrapper.eq(UnitDO::getRoundId, comp.getRoundId());
        List<UnitDO> unitDOS = unitDOMapper.selectList(queryWrapper);
        List<Unit> units = Collect.transfer(unitDOS, UnitDAOAdapter.Convertor.INST::to);
        return Result.success(units);
    }


    @PostMapping("centralizedBid")
    public Result<Void> centralizedBid(@RequestBody CentralizedBidPO centralizedBidPO, @RequestHeader("token") String token) {
        Integer userId = Integer.parseInt(TokenUtils.getUserId(token));
        Unit unit = domainTunnel.getByAggregateId(Unit.class, centralizedBidPO.getUnitId().toString());
        BizEx.trueThrow(Kit.notEq(unit.getUserId(), userId), ErrorEnums.PARAM_FORMAT_WRONG.message("无权限操作"));
        List<Bid> bids = Collect.transfer(centralizedBidPO.getBids(), Convertor.INST::to);
        bids.forEach(bid -> {
            bid.setUnitId(centralizedBidPO.getUnitId());
            bid.setProvince(unit.getMetaUnit().getProvince());
        });
        Comp comp = tunnel.runningComp();
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

}
