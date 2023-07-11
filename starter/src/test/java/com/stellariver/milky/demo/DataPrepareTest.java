package com.stellariver.milky.demo;

import com.stellariver.milky.demo.basic.Position;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.basic.UnitType;
import com.stellariver.milky.demo.infrastructure.database.entity.PodDO;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.PodDOMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.UserDOMapper;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class DataPrepareTest {


    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UniqueIdBuilder uniqueIdBuilder;

    @Autowired
    private PodDOMapper podDOMapper;

//    @Test
    public void prepareAdmin() {
        UserDO userDO0 = UserDO.builder()
                .name("root")
                .role(Role.ADMIN.name())
                .password("root")
                .userId("root")
                .build();
        userDOMapper.insert(userDO0);
    }

//    @Test
    public void prepareUsers() {
        UserDO userDO0 = UserDO.builder()
                .name("TOM")
                .role(Role.COMPETITOR.name())
                .password("TOM")
                .userId(uniqueIdBuilder.get().toString())
                .build();
        userDOMapper.insert(userDO0);

        UserDO userDO1 = UserDO.builder()
                .name("JACK")
                .role(Role.COMPETITOR.name())
                .password("JACK")
                .userId(uniqueIdBuilder.get().toString())
                .build();
        userDOMapper.insert(userDO1);
    }


//    @Test
    public void preparePods() {
        PodDO podDO00 = PodDO.builder()
                .podId(uniqueIdBuilder.get().toString())
                .podType(UnitType.GENERATOR.name())
                .podPos(Position.TRANSFER.name())
                .name("800MW机组_输电_TOM")
                .peakCapacity(300D)
                .flatCapacity(280D)
                .valleyCapacity(290D)
                .build();

        PodDO podDO01 = PodDO.builder()
                .podId(uniqueIdBuilder.get().toString())
                .podType(UnitType.GENERATOR.name())
                .podPos(Position.RECEIVE.name())
                .name("700MW机组_受电_TOM")
                .peakCapacity(300D)
                .flatCapacity(280D)
                .valleyCapacity(290D)
                .build();

        PodDO podDO02 = PodDO.builder()
                .podId(uniqueIdBuilder.get().toString())
                .podType(UnitType.LOAD.name())
                .podPos(Position.TRANSFER.name())
                .name("400MW负荷_输电_TOM")
                .peakCapacity(200D)
                .flatCapacity(380D)
                .valleyCapacity(180D)
                .build();

        PodDO podDO03 = PodDO.builder()
                .podId(uniqueIdBuilder.get().toString())
                .podType(UnitType.LOAD.name())
                .podPos(Position.RECEIVE.name())
                .name("600MW负荷_受电_TOM")
                .peakCapacity(200D)
                .flatCapacity(380D)
                .valleyCapacity(180D)
                .build();

        PodDO podDO04 = PodDO.builder()
                .podId(uniqueIdBuilder.get().toString())
                .podType(UnitType.GENERATOR.name())
                .podPos(Position.TRANSFER.name())
                .name("800MW机组_输电_JACK")
                .peakCapacity(300D)
                .flatCapacity(280D)
                .valleyCapacity(290D)
                .build();

        PodDO podDO05 = PodDO.builder()
                .podId(uniqueIdBuilder.get().toString())
                .podType(UnitType.GENERATOR.name())
                .podPos(Position.RECEIVE.name())
                .name("700MW机组_受电_JACK")
                .peakCapacity(300D)
                .flatCapacity(280D)
                .valleyCapacity(290D)
                .build();

        PodDO podDO06 = PodDO.builder()
                .podId(uniqueIdBuilder.get().toString())
                .podType(UnitType.LOAD.name())
                .podPos(Position.TRANSFER.name())
                .name("400MW负荷_输电_JACK")
                .peakCapacity(200D)
                .flatCapacity(380D)
                .valleyCapacity(180D)
                .build();

        PodDO podDO07 = PodDO.builder()
                .podId(uniqueIdBuilder.get().toString())
                .podType(UnitType.LOAD.name())
                .podPos(Position.RECEIVE.name())
                .name("600MW负荷_受电_JACK")
                .peakCapacity(200D)
                .flatCapacity(380D)
                .valleyCapacity(180D)
                .build();

        podDOMapper.insert(podDO00);
        podDOMapper.insert(podDO01);
        podDOMapper.insert(podDO02);
        podDOMapper.insert(podDO03);
        podDOMapper.insert(podDO04);
        podDOMapper.insert(podDO05);
        podDOMapper.insert(podDO06);
        podDOMapper.insert(podDO07);
    }


}

