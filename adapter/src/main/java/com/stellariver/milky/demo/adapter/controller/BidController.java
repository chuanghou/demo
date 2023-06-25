package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.Typed;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.adapter.controller.req.TransactionReq;
import com.stellariver.milky.demo.adapter.controller.req.YepBidReq;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.domain.command.YepBid;
import com.stellariver.milky.domain.support.command.CommandBus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author houchuang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("bid")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BidController {


    @PostMapping("yepBid")
    public Result<Void> YepBid(@RequestBody YepBidReq req, @RequestHeader("token") String token) {
        String userId = TokenUtils.getUserId(token);
        YepBid yepBid = YepBid.builder()
                .unitIdentify(UnitIdentify.resolve(req.getUnitId()))
                .transactions(Collect.transfer(req.getTransactionReqs(), Convertor.INST::to))
                .build();

        long count = yepBid.getTransactions().stream().map(Transaction::getDirection).distinct().count();
        BizEx.trueThrow(count != 1, ErrorEnums.PARAM_FORMAT_WRONG.message("年度集中竞价只能整体卖单或者买单!"));
        yepBid.setDirection(yepBid.getTransactions().get(0).getDirection());

        Map<Class<? extends Typed<?>>, Object> parameters = Collect.asMap(TypedEnums.USER_ID.class, userId);
        CommandBus.accept(yepBid, parameters);
        return Result.success();
    }


    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Transaction to(TransactionReq transactionReq);

    }

}
