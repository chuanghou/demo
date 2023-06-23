package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.BidType;
import com.stellariver.milky.demo.basic.Transaction;
import com.stellariver.milky.demo.basic.UnitIdentify;
import com.stellariver.milky.demo.domain.command.CentralizedBidCreate;
import com.stellariver.milky.demo.domain.command.RealTimeBidCreate;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.command.ConstructorHandler;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.catalina.Context;
import org.mapstruct.Builder;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bid extends AggregateRoot {

    String id;
    BidType type;
    UnitIdentify unitIdentify;
    Transaction transaction;


    @Override
    public String getAggregateId() {
        return id;
    }

    @ConstructorHandler
    static public Bid fromCentralizedBidden(CentralizedBidCreate create, Context context) {
        return Convertor.INST.toCentralized(create);
    }

    @ConstructorHandler
    static public Bid fromCentralizedBidden(RealTimeBidCreate create, Context context) {
        return Convertor.INST.toRealTime(create);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Bid toCentralized(CentralizedBidCreate create);

        @AfterMapping
        default void after(CentralizedBidCreate create, Bid bid) {
            bid.setType(BidType.Centralized);
        }

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Bid toRealTime(RealTimeBidCreate create);

        @AfterMapping
        default void after(RealTimeBidCreate create, Bid bid) {
            bid.setType(BidType.RealTime);
        }

    }


}
