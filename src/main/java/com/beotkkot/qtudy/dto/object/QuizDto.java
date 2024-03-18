package com.beotkkot.qtudy.dto.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private String question;
    private String answer;
    private List<String> options;
    private String explanation;
}
