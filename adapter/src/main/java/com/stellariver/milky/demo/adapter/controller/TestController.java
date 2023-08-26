package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.demo.basic.Message;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.basic.Topic;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestController {


    final Tunnel tunnel;

    @GetMapping("push")
    public Result<Void> push(@RequestHeader String token, @RequestParam String topic) {
        Message message = Message.builder().userId(TokenUtils.getUserId(token)).topic(Topic.valueOf(topic)).entity(topic).build();
        tunnel.push(message);
        return Result.success();
    }

}
