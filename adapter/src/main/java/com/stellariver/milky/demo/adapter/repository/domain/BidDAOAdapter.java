package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.demo.basic.BasicConvertor;
import com.stellariver.milky.demo.basic.Direction;
import com.stellariver.milky.demo.basic.Transaction;
import com.stellariver.milky.demo.domain.Bid;
import com.stellariver.milky.demo.infrastructure.database.entity.BidDO;
import com.stellariver.milky.demo.infrastructure.database.entity.PodDO;
import com.stellariver.milky.domain.support.dependency.DaoAdapter;
import com.stellariver.milky.domain.support.dependency.DataObjectInfo;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * @author houchuang
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BidDAOAdapter implements DaoAdapter<Bid> {

    @Override
    public Bid toAggregate(@NonNull Object dataObject) {
        return Convertor.INST.to((BidDO) dataObject);
    }

    @Override
    public Object toDataObject(Bid bid, DataObjectInfo dataObjectInfo) {
        return Convertor.INST.to(bid);
    }

    @Override
    public DataObjectInfo dataObjectInfo(String aggregateId) {
        return DataObjectInfo.builder().clazz(PodDO.class).primaryId(aggregateId).build();
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor extends BasicConvertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        @Mapping(source = "unitId", target = "unitIdentify")
        Bid to(BidDO bidDO);

        @AfterMapping
        default void after(BidDO bidDO, @MappingTarget Bid bid) {
            Transaction transaction = Transaction.builder()
                    .direction(Enum.valueOf(Direction.class, bidDO.getDirection()))
                    .price(bidDO.getPrice())
                    .quantity(bidDO.getQuantity())
                    .build();
            bid.setTransaction(transaction);
        }

        @BeanMapping(builder = @Builder(disableBuilder = true))
        @Mapping(source = "unitIdentify.podId", target = "podId")
        @Mapping(source = "unitIdentify", target = "unitId")
        @Mapping(source = "transaction.direction", target = "direction")
        @Mapping(source = "transaction.quantity", target = "quantity")
        @Mapping(source = "transaction.price", target = "price")
        BidDO to(Bid bid);

    }
}
