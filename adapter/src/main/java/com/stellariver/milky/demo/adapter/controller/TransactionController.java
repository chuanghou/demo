package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.BeanUtil;
import com.stellariver.milky.demo.adapter.controller.req.CentralizedBidReq;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.domain.command.CentralizedBid;
import com.stellariver.milky.domain.support.command.CommandBus;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author houchuang
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("interProvince")
public class TransactionController {

    @PostMapping("centralizedBid")
    public Result<String> centralizedBid(@RequestBody CentralizedBidReq req, @RequestHeader("token") String token) {
        String agentId = TokenUtils.getAgentId(token);
        CentralizedBid centralizedBid = Convertor.INST.to(req);
        CommandBus.accept(centralizedBid, new HashMap<>());
        return Result.success(agentId);
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CentralizedBid to(CentralizedBidReq req);

        @AfterMapping
        default void after(CentralizedBidReq req, CentralizedBid centralizedBid) {
            centralizedBid.setBidId(BeanUtil.getBean(UniqueIdBuilder.class).get().toString());
        }

    }
}
