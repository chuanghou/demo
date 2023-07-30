package com.stellariver.milky.demo.domain.tunnel;

import com.stellariver.milky.demo.domain.AbstractMetaUnit;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Unit;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Tunnel {

    List<Unit> listUnitsByCompId(Long compId);

    AbstractMetaUnit getByMetaUnitId(String metaUnitId);

    long loadGeneratorNumber();

    long loadLoadNumber();

    long loadUnitNumber();

    Map<Integer, AbstractMetaUnit> getMetaUnitsByIds(Set<Integer> metaUnitIds);

    Set<Integer> getMetaUnitIdBySourceIds(Set<Integer> metaUnitSourceIds);


    Comp runningComp();
}
