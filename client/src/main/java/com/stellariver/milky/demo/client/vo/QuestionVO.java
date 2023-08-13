package com.stellariver.milky.demo.client.vo;

import com.stellariver.milky.demo.common.enums.QuestionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionVO {

    Long id;

    Integer uid;

    String question;

    QuestionType questionType;

    Map<String, Object> options;

}
