package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.demo.basic.Message;
import com.stellariver.milky.demo.basic.Topic;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestController {


    final Tunnel tunnel;

    @GetMapping("push")
    public void push(String value) {
        log.info(value);
        Message message = Message.builder().userId("0").topic(Topic.RT_COMP).entity(value).build();
        tunnel.push(message);
    }

}
