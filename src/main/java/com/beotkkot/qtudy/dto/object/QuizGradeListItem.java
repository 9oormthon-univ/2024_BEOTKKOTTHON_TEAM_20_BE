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
public class QuizGradeListItem {
    private Long quizId;
    private List<String> tags;
    private String question;
    private String answer;
    private boolean correct;
    private int userAnswer;
    private List<String> options;
    private String explanation;

    public static QuizGradeListItem of(Quiz quiz, boolean correct, int userAnswer) {
        List<String> tags = Arrays.asList(quiz.getTags().split("\\s*,\\s*"));
        List<String> options = Arrays.asList(quiz.getOptions().split("\\s*,\\s*"));

        return QuizGradeListItem.builder()
                .quizId(quiz.getQuizId())
                .tags(tags)
                .question(quiz.getQuestion())
                .answer(quiz.getAnswer())
                .correct(correct)
                .userAnswer(userAnswer)
                .options(options)
                .explanation(quiz.getExplanation())
                .build();
    }
}
