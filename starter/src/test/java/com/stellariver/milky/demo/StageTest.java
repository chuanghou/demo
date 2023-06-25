package com.stellariver.milky.demo;

import com.stellariver.milky.demo.adapter.controller.CompController;
import com.stellariver.milky.demo.basic.Role;
import com.stellariver.milky.demo.infrastructure.database.entity.UserDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.UserDOMapper;
import com.stellariver.milky.spring.partner.UniqueIdBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class StageTest {


    @Autowired
    private CompController compController;

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UniqueIdBuilder uniqueIdBuilder;

    @Test
    public void testToken() {
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

}

