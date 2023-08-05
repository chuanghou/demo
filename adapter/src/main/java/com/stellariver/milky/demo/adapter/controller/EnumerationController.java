package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.Enumeration;
import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.common.Status;
import com.stellariver.milky.demo.common.enums.Direction;
import com.stellariver.milky.demo.common.enums.Province;
import com.stellariver.milky.demo.common.enums.TimeFrame;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("enumerations")
public class EnumerationController {

    @GetMapping("listProvinces")
    public List<Enumeration> listProvinces() {
        return Arrays.stream(Province.values()).map(e -> new Enumeration(e.name(), e.getDesc())).collect(Collectors.toList());
    }

    @GetMapping("listUnitTypes")
    public List<Enumeration> listUnitTypes() {
        return Arrays.stream(UnitType.values()).map(e -> new Enumeration(e.name(), e.getDesc())).collect(Collectors.toList());
    }

    @GetMapping("listDirections")
    public List<Enumeration> listDirections() {
        return Arrays.stream(Direction.values()).map(e -> new Enumeration(e.name(), e.getDesc())).collect(Collectors.toList());
    }

    @GetMapping("listTimeFrames")
    public List<Enumeration> listTimeFrames() {
        return Arrays.stream(TimeFrame.values()).map(e -> new Enumeration(e.name(), e.getDesc())).collect(Collectors.toList());
    }

    @GetMapping("listMarketStatuses")
    public List<Enumeration> listMarketStatuses() {
        return Arrays.stream(Status.MarketStatus.values()).map(e -> new Enumeration(e.name(), e.getDesc())).collect(Collectors.toList());
    }


}
