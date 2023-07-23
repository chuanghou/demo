package com.stellariver.milky.demo.adapter.repository.domain;

import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.domain.support.dependency.DaoAdapter;
import com.stellariver.milky.domain.support.dependency.DataObjectInfo;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author houchuang
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompDAOAdapter implements DaoAdapter<Comp> {

    @Override
    public Comp toAggregate(@NonNull Object dataObject) {
        return (Comp) dataObject;
    }

    @Override
    public Object toDataObject(Comp comp, DataObjectInfo dataObjectInfo) {
        return comp;
    }

    @Override
    public DataObjectInfo dataObjectInfo(String aggregateId) {
        return DataObjectInfo.builder().clazz(Comp.class).primaryId(Long.parseLong(aggregateId)).build();
    }



}
