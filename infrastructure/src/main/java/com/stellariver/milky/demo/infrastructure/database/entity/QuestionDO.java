package com.stellariver.milky.demo.infrastructure.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@TableName("examination_questions")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionDO {

  Long id;
  String name;
  Integer type;
  Integer uid;
  String options;
  Integer status;
  String answers;
  Date addTime;

}
