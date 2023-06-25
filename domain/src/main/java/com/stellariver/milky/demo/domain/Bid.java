package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.basic.BidType;
import com.stellariver.milky.demo.basic.Transaction;
import com.stellariver.milky.demo.basic.UnitIdentify;
import com.stellariver.milky.demo.domain.command.CentralizedBidbuild;
import com.stellariver.milky.demo.domain.command.RealTimeBidCreate;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.command.ConstructorHandler;
import com.stellariver.milky.domain.support.context.Context;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.mapstruct.*;
import org.mapstruct.Builder;
import org.mapstruct.factory.Mappers;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bid extends AggregateRoot {

    String bidId;
    BidType type;
    UnitIdentify unitIdentify;
    Transaction transaction;


    @Override
    public String getAggregateId() {
        return bidId;
    }

    @ConstructorHandler
    static public Bid fromCentralizedBidden(CentralizedBidbuild create, Context context) {
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
        Bid toCentralized(CentralizedBidbuild build);

        @AfterMapping
        default void after(CentralizedBidbuild build, Bid bid) {
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
