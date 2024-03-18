package com.beotkkot.qtudy.dto.request.quiz;

import com.beotkkot.qtudy.dto.object.QuizDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostQuizRequestDto {
    private Long postId;
    private String tags;
    private QuizDto quizDto;
}
