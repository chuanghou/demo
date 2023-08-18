package com.stellariver.milky.demo.adapter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stellariver.milky.common.base.BizEx;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.common.tool.util.Collect;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.TokenUtils;
import com.stellariver.milky.demo.client.po.AnswerPO;
import com.stellariver.milky.demo.common.ExamScoreVO;
import com.stellariver.milky.demo.common.QuestionVO;
import com.stellariver.milky.demo.common.enums.QuestionType;
import com.stellariver.milky.demo.domain.tunnel.Tunnel;
import com.stellariver.milky.demo.infrastructure.database.entity.DemoExamScore;
import com.stellariver.milky.demo.infrastructure.database.entity.QuestionDO;
import com.stellariver.milky.demo.infrastructure.database.mapper.DemoExamScoreMapper;
import com.stellariver.milky.demo.infrastructure.database.mapper.QuestionDOMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("exam")
@RequiredArgsConstructor
public class ExamController {

    final Tunnel tunnel;

    final QuestionDOMapper questionDOMapper;

    @GetMapping("getExamVO")
    public Result<DemoExamScore> getExamVO(@RequestHeader String token) {
        Long compId = tunnel.runningComp().getCompId();
        Integer userId = Integer.parseInt(TokenUtils.getUserId(token));

        LambdaQueryWrapper<DemoExamScore> eq = new LambdaQueryWrapper<DemoExamScore>().eq(DemoExamScore::getCompId, compId).eq(DemoExamScore::getUserId, userId);
        DemoExamScore demoExamScore = demoExamScoreMapper.selectOne(eq);
        if (demoExamScore != null) {
            return Result.success(demoExamScore);
        }

        List<QuestionDO> questionDOs = questionDOMapper.selectList(null);
        int paperNo = tunnel.runningComp().getPaperNo() - 1;
        questionDOs = questionDOs.stream().filter(q -> q.getUid() % 5 == paperNo)
                .sorted(Comparator.comparing(QuestionDO::getType)).collect(Collectors.toList());

        List<QuestionVO> questionVOs = Collect.transfer(questionDOs, q -> {
            Map<String, Object> rawOptions = Json.parseMap(q.getOptions(), String.class, Object.class);
            HashMap<String, Object> options = new HashMap<>();
            rawOptions.entrySet().stream().filter(e -> !((e.getValue() instanceof String) && ((StringUtils.isBlank((String) e.getValue())))))
                    .forEach(e -> options.put(e.getKey(), e.getValue()));
            return QuestionVO.builder().id(q.getId())
                    .ratio(Kit.enumOfMightEx(QuestionType::getDbCode, q.getType()).getRatio())
                    .question(q.getName())
                    .options(options)
                    .questionType(Kit.enumOfMightEx(QuestionType::getDbCode, q.getType()))
                    .uid(q.getUid())
                    .build();
        });

        ExamScoreVO examScoreVO = ExamScoreVO.builder().questionVOs(questionVOs).build();
        demoExamScore = DemoExamScore.builder().compId(compId).userId(userId).score(examScoreVO).build();
        return Result.success(demoExamScore);
    }

    @PostMapping("submitAnswers")
    public Result<Void> submitAnswers(@RequestBody List<AnswerPO> answerPOs, @RequestHeader String token) {

        Long compId = tunnel.runningComp().getCompId();
        Integer userId = Integer.parseInt(TokenUtils.getUserId(token));

        List<QuestionDO> questionDOs = questionDOMapper.selectList(null);
        int paperNo = tunnel.runningComp().getPaperNo() - 1;
        questionDOs = questionDOs.stream().filter(q -> q.getUid() % 5 == paperNo)
                .sorted(Comparator.comparing(QuestionDO::getType)).collect(Collectors.toList());

        List<QuestionVO> questionVOs = Collect.transfer(questionDOs, q -> {
            Map<String, Object> rawOptions = Json.parseMap(q.getOptions(), String.class, Object.class);
            HashMap<String, Object> options = new HashMap<>();
            rawOptions.entrySet().stream().filter(e -> !((e.getValue() instanceof String) && ((StringUtils.isBlank((String) e.getValue())))))
                    .forEach(e -> options.put(e.getKey(), e.getValue()));
            return QuestionVO.builder().id(q.getId())
                    .ratio(Kit.enumOfMightEx(QuestionType::getDbCode, q.getType()).getRatio())
                    .question(q.getName())
                    .options(options)
                    .questionType(Kit.enumOfMightEx(QuestionType::getDbCode, q.getType()))
                    .uid(q.getUid())
                    .build();
        });

        Integer score = questionVOs.stream()
                .map(q -> (q.getAnswers().equals(q.getChoices()) ? 1 : 0) * q.getRatio()).reduce(0, Integer::sum);
        ExamScoreVO examScoreVO = ExamScoreVO.builder().score(score).questionVOs(questionVOs).build();
        DemoExamScore demoExamScore = DemoExamScore.builder().compId(compId).userId(userId).score(examScoreVO).build();
        demoExamScoreMapper.insert(demoExamScore);
        return Result.success();
    }

    final DemoExamScoreMapper demoExamScoreMapper;

}
