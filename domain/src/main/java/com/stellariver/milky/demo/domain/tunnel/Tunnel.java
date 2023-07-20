package com.stellariver.milky.demo.domain.tunnel;

import com.stellariver.milky.demo.domain.MetaUnit;
import com.stellariver.milky.demo.domain.Unit;

import java.util.List;

public interface Tunnel {

    List<Unit> getByCompId(Integer compId);

    MetaUnit getByMetaUnitId(String metaUnitId);

    long loadGeneratorNumber();

    long loadLoadNumber();

    long loadUnitNumber();

}
