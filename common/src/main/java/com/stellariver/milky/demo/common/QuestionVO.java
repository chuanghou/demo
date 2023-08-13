package com.stellariver.milky.demo.common;

import com.stellariver.milky.demo.common.enums.QuestionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionVO {

    Long id;

    Integer uid;

    String question;

    Integer ratio;

    QuestionType questionType;

    Map<String, Object> options;

    Set<String> choices;

    Set<String> answers;

}
