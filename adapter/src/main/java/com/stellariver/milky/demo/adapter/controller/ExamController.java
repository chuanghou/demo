package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.client.po.AnswerPO;
import com.stellariver.milky.demo.client.vo.QuestionVO;
import com.stellariver.milky.demo.common.enums.QuestionType;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.QuestionDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.QuestionDOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("exam")
@RequiredArgsConstructor
public class ExamController {

    final Tunnel tunnel;

    final QuestionDOMapper questionDOMapper;

    @GetMapping("listQuestions")
    public Result<List<QuestionVO>> listQuestions() {
        List<QuestionDO> questionDOs = questionDOMapper.selectList(null);
        List<QuestionVO> questionVOs = Collect.transfer(questionDOs, q -> QuestionVO.builder().id(q.getId())
                .options(Json.parseMap(q.getOptions(), String.class, Object.class))
                .questionType(Kit.enumOfMightEx(QuestionType::getDbCode, q.getType()))
                .uid(q.getUid())
                .build());
        int paperNo = tunnel.runningComp().getPaperNo() - 1;
        questionVOs = questionVOs.stream().filter(q -> q.getUid() % 5 == paperNo).collect(Collectors.toList());
        return Result.success(questionVOs);
    }

    @GetMapping("submitAnswers")
    public void submitAnswers(@RequestBody List<AnswerPO> answerPOs, @RequestHeader String token) {

    }



}
