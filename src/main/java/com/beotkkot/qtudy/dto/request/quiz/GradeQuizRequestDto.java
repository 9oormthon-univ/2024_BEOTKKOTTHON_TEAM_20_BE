package com.beotkkot.qtudy.dto.request.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeQuizRequestDto {
    private List<Long> quizIdList;
    private List<String> answerList;
    private List<Integer> userAnswerList;
}
