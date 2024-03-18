package com.beotkkot.qtudy.dto.object;

import com.beotkkot.qtudy.domain.quiz.Quiz;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizListItem {
    private Long quizId;
    private List<String> tags;
    private String question;
    private String answer;
    private List<String> options;
    private String explanation;

    public static QuizListItem of(Quiz quiz) {
        List<String> tags = Arrays.asList(quiz.getTags().split("\\s*,\\s*"));
        List<String> options = Arrays.asList(quiz.getOptions().split("\\s*,\\s*"));

        return QuizListItem.builder()
                .quizId(quiz.getQuizId())
                .tags(tags)
                .question(quiz.getQuestion())
                .answer(quiz.getAnswer())
                .options(options)
                .explanation(quiz.getExplanation())
                .build();
    }
}
