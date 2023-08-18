package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.stellariver.milky.demo.common.ExamScoreVO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("demo_exam_score")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemoExamScore {

  Long compId;
  Integer userId;
  @TableField(typeHandler = JacksonTypeHandler.class)
  ExamScoreVO examScoreVO;

}
