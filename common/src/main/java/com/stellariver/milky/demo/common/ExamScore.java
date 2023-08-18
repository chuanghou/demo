package com.stellariver.milky.demo.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamScore {

    Integer score;
    List<QuestionVO> questionVOs;

}


