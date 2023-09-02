package com.stellariver.milky.demo.adapter.repository.domain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.BeanUtil;
import com.stellariver.milky.common.base.SysEx;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.demo.basic.BasicConvertor;
import com.stellariver.milky.demo.common.enums.UnitType;
import com.stellariver.milky.demo.common.Bid;
import com.stellariver.milky.demo.domain.MetaUnit;
import com.stellariver.milky.demo.domain.GeneratorMetaUnit;
import com.stellariver.milky.demo.domain.LoadMetaUnit;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.infrastructure.database.entity.BidDO;
import com.stellariver.milky.demo.infrastructure.database.entity.MetaUnitDO;
import com.stellariver.milky.demo.infrastructure.database.entity.UnitDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.BidDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.MetaUnitDOMapper;
import com.stellariver.milky.domain.support.dependency.DaoAdapter;
import com.stellariver.milky.domain.support.dependency.DataObjectInfo;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author houchuang
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UnitDAOAdapter implements DaoAdapter<Unit> {

    final MetaUnitDOMapper metaUnitDOMapper;

    @Override
    public Unit toAggregate(@NonNull Object dataObject) {
        return Convertor.INST.to((UnitDO) dataObject);
    }

    @Override
    public Object toDataObject(Unit unit, DataObjectInfo dataObjectInfo) {
        return Convertor.INST.to(unit);
    }

    @Override
    public DataObjectInfo dataObjectInfo(String aggregateId) {
        return DataObjectInfo.builder().clazz(UnitDO.class).primaryId(Long.parseLong(aggregateId)).build();
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface Convertor extends BasicConvertor {

        Convertor INST = Mappers.getMapper(Convertor.class);

        @BeanMapping(builder = @Builder(disableBuilder = true))
        Unit to(UnitDO unitDO);

        @AfterMapping
        default void after(UnitDO unitDO, @MappingTarget Unit unit) {
            MetaUnitDOMapper metaUnitDOMapper = BeanUtil.getBean(MetaUnitDOMapper.class);
            MetaUnitDO metaUnitDO = metaUnitDOMapper.selectById(unitDO.getMetaUnitId());
            SysEx.nullThrow(metaUnitDO.getUnitType());
            unit.setMetaUnit(to(metaUnitDO));
            BidDOMapper bidDOMapper = BeanUtil.getBean(BidDOMapper.class);
            LambdaQueryWrapper<BidDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BidDO::getUnitId, unit.getUnitId());
            List<BidDO> bidDOs = bidDOMapper.selectList(queryWrapper);
            List<Bid> bids = Collect.transfer(bidDOs, Convertor.INST::toBid);
            unit.setBids(Collect.toMapMightEx(bids, Bid::getBidId));
        }

        default MetaUnit to(MetaUnitDO metaUnitDO) {
            boolean b = Kit.eq(metaUnitDO.getUnitType(), UnitType.GENERATOR.name());
            return b ? Convertor.INST.toGeneratorMetaUnit(metaUnitDO) : Convertor.INST.toLoadMetaUnit(metaUnitDO);
        }

        @BeanMapping(builder = @Builder(disableBuilder = true))
        UnitDO to(Unit unit);

        @AfterMapping
        default void after(Unit unit, @MappingTarget UnitDO unitDO) {
            unitDO.setMetaUnitId(unit.getMetaUnit().getMetaUnitId());
            unitDO.setProvince(unit.getMetaUnit().getProvince().name());
            unitDO.setUnitType(unit.getMetaUnit().getUnitType().name());
            List<BidDO> bidDOs = Collect.transfer(unit.getBids().values(), Convertor.INST::fromBid);
            unitDO.setBidDOs(bidDOs);
        }

        @BeanMapping(builder = @Builder(disableBuilder = true))
        GeneratorMetaUnit toGeneratorMetaUnit(MetaUnitDO metaUnitDO);


        @BeanMapping(builder = @Builder(disableBuilder = true))
        LoadMetaUnit toLoadMetaUnit(MetaUnitDO metaUnitDO);


        @BeanMapping(builder = @Builder(disableBuilder = true))
        BidDO fromBid(Bid bid);


        @BeanMapping(builder = @Builder(disableBuilder = true))
        Bid toBid(BidDO bidDO);


    }


}
