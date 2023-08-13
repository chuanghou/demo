package com.stellariver.milky.demo.domain.tunnel;

import com.stellariver.milky.demo.basic.CentralizedDeals;
import com.stellariver.milky.demo.basic.Message;
import com.stellariver.milky.demo.common.MarketType;
import com.stellariver.milky.demo.common.enums.TimeFrame;
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


    void push(Message message);

    Comp runningComp();

    void updateRoundIdForMarketSetting(Integer roundId);

    void tieLinePower(Integer roundId, MarketType marketType, Map<TimeFrame, Double> replenishes, Map<TimeFrame, CentralizedDeals> centralizedDealsMap);

    void cast(Message message);
}
