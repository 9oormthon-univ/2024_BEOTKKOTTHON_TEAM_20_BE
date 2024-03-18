package com.beotkkot.qtudy.dto.request.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateQuizRequestDto {
    private Long postId;
    private String tags;
    private String summary;
}
