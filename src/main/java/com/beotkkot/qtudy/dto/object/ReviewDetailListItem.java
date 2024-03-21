package com.beotkkot.qtudy.dto.object;

import com.beotkkot.qtudy.domain.quiz.Quiz;
import com.beotkkot.qtudy.domain.quiz.Review;
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
public class ReviewDetailListItem {
    private Long quizId;
    private List<String> tags;
    private String question;
    private String answer;
    private int userAnswer;
    private boolean correct;
    private List<String> options;
    private String explanation;
    private String createdAt;

    public static ReviewDetailListItem of(Quiz quiz, Review review) {
        List<String> tags = Arrays.asList(quiz.getTags().split("\\s*,\\s*"));
        List<String> options = Arrays.asList(quiz.getOptions().split("\\s*,\\s*"));

        return ReviewDetailListItem.builder()
                .quizId(quiz.getQuizId())
                .tags(tags)
                .question(quiz.getQuestion())
                .answer(quiz.getAnswer())
                .userAnswer(review.getUserAnswer())
                .correct(review.isCorrect())
                .options(options)
                .explanation(quiz.getExplanation())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
