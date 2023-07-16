//package com.stellariver.milky.demo.adapter.repository.domain;
//
//import com.stellariver.milky.common.tool.common.Kit;
//import com.stellariver.milky.demo.basic.BasicConvertor;
//import com.stellariver.milky.demo.common.MarketType;
//import com.stellariver.milky.demo.domain.Comp;
//import com.stellariver.milky.demo.infrastructure.database.entity.CompDO;
//import com.stellariver.milky.domain.support.dependency.DaoAdapter;
//import com.stellariver.milky.domain.support.dependency.DataObjectInfo;
//import lombok.AccessLevel;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.mapstruct.*;
//import org.mapstruct.factory.Mappers;
//
///**
// * @author houchuang
// */
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class CompDAOAdapter implements DaoAdapter<Comp> {
//
//    @Override
//    public Comp toAggregate(@NonNull Object dataObject) {
//        return (Comp) ((CompDO) dataObject).getComp();
//    }
//
//    @Override
//    public Object toDataObject(Comp comp, DataObjectInfo dataObjectInfo) {
//        return Convertor.INST.to(comp);
//    }
//
//    @Override
//    public DataObjectInfo dataObjectInfo(String aggregateId) {
//        return DataObjectInfo.builder().clazz(CompDO.class).primaryId(aggregateId).build();
//    }
//
//    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
//            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    public interface Convertor extends BasicConvertor {
//
//        Convertor INST = Mappers.getMapper(Convertor.class);
//
//        @BeanMapping(builder = @Builder(disableBuilder = true))
//        Comp to(CompDO compDO);
//
//        @BeanMapping(builder = @Builder(disableBuilder = true))
//        CompDO to(Comp comp);
//
//        default MarketType toMarketType(Integer marketType) {
//            return Kit.enumOfMightEx(MarketType::getDbCode, marketType);
//        }
//
//
//        default Integer fromMarketType(MarketType marketType) {
//            return marketType.getDbCode();
//        }
//    }
//}
