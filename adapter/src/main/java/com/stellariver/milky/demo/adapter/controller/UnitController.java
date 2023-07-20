package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.BeanUtil;
import com.stellariver.milky.common.tool.common.Clock;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.common.Typed;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.repository.domain.UnitDAOAdapter;
import com.stellariver.milky.demo.basic.AgentConfig;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.basic.TypedEnums;
import com.stellariver.milky.demo.client.po.BidPO;
import com.stellariver.milky.demo.client.po.CentralizedBidPO;
import com.stellariver.milky.demo.client.po.RealtimeBidPO;
import com.stellariver.milky.demo.client.vo.OrderVO;
import com.stellariver.milky.demo.client.vo.UnitVO;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.common.Order;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.domain.command.UnitCommand;
import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.CompDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.UnitDOMapper;
import com.stellariver.milky.domain.support.base.DomainTunnel;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Builder;
import org.mapstruct.Mapping;
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

    final CompDOMapper compDOMapper;
    @GetMapping
    public Result<PriceLimit> getPriceLimit(@RequestParam String marketTypeValue,
                                           @RequestHeader("token") String token ) {
        CompDO compDO = compDOMapper.selectById(1);
        PriceLimit priceLimit = PriceLimit.builder()
                .generatorHighLimit(compDO.getOfferPriceCap())
                .generatorLowLimit(compDO.getOfferPriceFloor())
                .loadHighLimit(compDO.getOfferPriceCap())
                .loadLowLimit(compDO.getOfferPriceFloor())
                .build();
        return Result.success(priceLimit);
    }

    @Data
    @lombok.Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static public class PriceLimit{
        Double generatorLowLimit;
        Double generatorHighLimit;
        Double loadLowLimit;
        Double loadHighLimit;
    }

    @GetMapping
    public Result<Void> getCentralizedUnit(@RequestParam String marketTypeValue,
                                           @RequestHeader("token") String token ) {
        String userId = TokenUtils.getUserId(token);
        Comp comp = domainTunnel.getByAggregateId(Comp.class, "1");
        AgentConfig agentConfig = comp.getAgentConfigs().stream().filter(ac -> Kit.eq(ac.getAgentId(), userId))
                .findFirst().orElseThrow(() -> new BizEx(ErrorEnums.CONFIG_ERROR.message("不存在相应用户")));
        agentConfig.getGeneratorId0()
    }

    @PostMapping
    public Result<Void>




    @GetMapping("listUnits")
    public Result<List<UnitVO>> listOrders(@RequestHeader("token") String token) {
        String userId = TokenUtils.getUserId(token);
        Comp comp = domainTunnel.getByAggregateId(Comp.class, "1");
        comp.
        List<Unit> units = Collect.transfer(unitDOs, UnitDAOAdapter.Convertor.INST::to);

        return Result.success(unitVOs);
    }



    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        @Mapping(source = "txGroupPO", target = "txGroup")
        Bid to(BidPO bidPO);

        @AfterMapping
        default void after(BidPO bidPO, @MappingTarget Bid bid) {
            bid.setId(BeanUtil.getBean(UniqueIdBuilder.class).get().toString());
            bid.setDate(Clock.now());
        }

        @BeanMapping(builder = @Builder(disableBuilder = true))
        @Mapping(source = "txGroup", target = "txGroupVO")
        OrderVO toOrderVO(Order order);

    }

}
