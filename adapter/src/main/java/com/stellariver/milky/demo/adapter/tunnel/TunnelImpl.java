package com.stellariver.milky.demo.adapter.tunnel;

import com.stellariver.milky.demo.domain.AbstractMetaUnit;
import com.stellariver.milky.demo.domain.Comp;
import com.stellariver.milky.demo.domain.Unit;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TunnelImpl implements Tunnel {


    @Override
    public List<Unit> getByCompId(Long compId) {
        return null;
    }

    @Override
    public List<Unit> listUnitsByCompId(Long compId) {
        return null;
    }

    @Override
    public AbstractMetaUnit getByMetaUnitId(String metaUnitId) {
        return null;
    }

    @Override
    public long loadGeneratorNumber() {
        return 0;
    }

    @Override
    public long loadLoadNumber() {
        return 0;
    }

    @Override
    public long loadUnitNumber() {
        return 0;
    }

    @Override
    public Map<Long, AbstractMetaUnit> getMetaUnitsByIds(Set<Long> metaUnitIds) {
        return null;
    }

    @Override
    public Comp currentComp() {
        return null;
    }
}
