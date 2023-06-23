package com.stellariver.milky.demo.domain;

import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.demo.basic.*;
import com.stellariver.milky.demo.domain.command.CentralizedBid;
import com.stellariver.milky.demo.domain.event.CentralizedBiddenEvent;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.context.Context;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionUnit extends AggregateRoot {

    UnitIdentify unitIdentify;
    Double capacity;
    Double bought;
    Double sold;

    @Override
    public String getAggregateId() {
        return unitIdentify.getUnitId();
    }


    public void handle(CentralizedBid centralizedBid, Context context) {

        Transaction transaction = centralizedBid.getTransaction();

        boolean generatorSell = (Type.GENERATOR == unitIdentify.getType()) && (transaction.getDirection() == Direction.SELL);
        boolean loadBuy = (Type.LOAD == unitIdentify.getType()) && (transaction.getDirection() == Direction.BUY);

        BizEx.falseThrow(generatorSell || loadBuy, ErrorEnums.PARAM_FORMAT_WRONG.message("参数异常" + centralizedBid.toString()));

        CentralizedBiddenEvent event = null;
        Double balance = null;

        if (generatorSell) {
            balance = capacity + bought - sold;
        }

        if (loadBuy) {
            balance = capacity + sold - bought;
        }

        if (transaction.getQuantity() <= balance) {
            sold += transaction.getQuantity();
            event = CentralizedBiddenEvent.builder().unitIdentify(unitIdentify).transaction(transaction).build();
        }

        if (event != null) {
            context.publish(event);
        }
    }

}
