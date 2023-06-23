package com.stellariver.milky.demo;

import com.stellariver.milky.demo.basic.TokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenTest {


    @Test
    public void testToken() {
        String test = TokenUtils.sign("test");
        String agentId = TokenUtils.getAgentId(test);
        Assertions.assertEquals("test", agentId);
    }



}

