package com.stellariver.milky.demo;

import com.stellariver.milky.demo.adapter.controller.CompController;
import com.stellariver.milky.demo.adapter.controller.req.AddCompReq;
import com.stellariver.milky.demo.adapter.controller.resp.CompResp;
import com.stellariver.milky.demo.basic.Agent;
import com.stellariver.milky.demo.basic.Stage;
import com.stellariver.milky.demo.basic.TokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@SpringBootTest
public class BidTest {

    @Autowired
    CompController compController;

    @Test
    public void testComp() {

        Agent agent00 = Agent.builder().userId("401").podIds(Arrays.asList("1201", "1202", "1203", "1204")).build();
        Agent agent01 = Agent.builder().userId("402").podIds(Arrays.asList("1205", "1206", "1207", "1208")).build();


        AddCompReq addCompReq00 = AddCompReq.builder()
                .name("第一轮竞赛")
                .date("20230616")
                .agents(Arrays.asList(agent00, agent01))
                .build();
        String rootToken = TokenUtils.sign("root");
        compController.addComp(addCompReq00, rootToken);

        List<CompResp> compResps = compController.listComps().getData();
        Assertions.assertEquals(compResps.size(), 1);
        CompResp compResp = compResps.get(0);
        Assertions.assertEquals(compResp.getName(), "第一轮竞赛");
        Assertions.assertEquals(compResp.getStage(), Stage.INITIALIZED.name());

        AddCompReq addCompReq01 = AddCompReq.builder()
                .name("第二轮竞赛")
                .date("20230616")
                .agents(Arrays.asList(agent00, agent01))
                .build();
        compController.addComp(addCompReq01, rootToken);

        compResps = compController.listComps().getData();
        Assertions.assertEquals(compResps.size(), 2);

        AddCompReq addCompReq02 = AddCompReq.builder()
                .name("第三轮竞赛")
                .date("20230616")
                .agents(Arrays.asList(agent00, agent01))
                .build();
        compController.addComp(addCompReq02, rootToken);

        compResps = compController.listComps().getData();
        Assertions.assertEquals(compResps.size(), 3);

    }


    @Test
    public void testBid() {

        Agent agent00 = Agent.builder().userId("401").podIds(Arrays.asList("1201", "1202", "1203", "1204")).build();
        Agent agent01 = Agent.builder().userId("402").podIds(Arrays.asList("1205", "1206", "1207", "1208")).build();


        AddCompReq addCompReq00 = AddCompReq.builder()
                .name("第一轮竞赛")
                .date("20230616")
                .agents(Arrays.asList(agent00, agent01))
                .build();
        String rootToken = TokenUtils.sign("root");
        compController.addComp(addCompReq00, rootToken);

        List<CompResp> compResps = compController.listComps().getData();
        Assertions.assertEquals(compResps.size(), 1);
        CompResp compResp = compResps.get(0);




    }

}
