package com.beotkkot.qtudy.dto.request.quiz;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    private String role;
    private String content;
}
