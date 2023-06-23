package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.demo.adapter.controller.req.CentralizedBidReq;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.domain.command.CentralizedBid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

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

        return Result.success(agentId);
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        CentralizedBid to(CentralizedBidReq req);

    }
}
