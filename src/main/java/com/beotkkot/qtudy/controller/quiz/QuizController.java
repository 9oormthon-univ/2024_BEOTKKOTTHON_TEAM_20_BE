package com.beotkkot.qtudy.controller.quiz;

import com.beotkkot.qtudy.dto.request.quiz.GenerateQuizRequestDto;
import com.beotkkot.qtudy.dto.request.quiz.GradeQuizRequestDto;
import com.beotkkot.qtudy.dto.response.quiz.GetPostQuizResponseDto;
import com.beotkkot.qtudy.dto.response.quiz.QuizGradeResponseDto;
import com.beotkkot.qtudy.service.quiz.QuizService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
@RestController
@Slf4j
public class QuizController {

    private final QuizService quizService;

//     퀴즈 생성 및 저장
    @PostMapping("/quiz")
    public ResponseEntity<?> generateQuiz(@RequestBody GenerateQuizRequestDto dto) {
        try {
            return ResponseEntity.ok().body(quizService.generateQuiz(dto));
        } catch (HttpClientErrorException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 태그별 퀴즈 출력
    @GetMapping("/quiz/tag-quiz/{tagName}")
    public ResponseEntity<? super GetPostQuizResponseDto> getTagQuiz(@PathVariable String tagName) {
        ResponseEntity<? super GetPostQuizResponseDto> response = quizService.getTagQuiz(tagName);
        return response;
    }

    // 게시글 별 퀴즈 출력
    @GetMapping("/quiz/post-quiz/{postId}")
    public ResponseEntity<? super GetPostQuizResponseDto> getPostQuiz(@PathVariable Long postId) {
        ResponseEntity<? super GetPostQuizResponseDto> response = quizService.getPostQuiz(postId);
        return response;
    }

    // 정답 채점
    @PostMapping("/quiz/grade")
    public ResponseEntity<? super QuizGradeResponseDto> gradeQuiz(@RequestBody GradeQuizRequestDto dto) {
        ResponseEntity<? super QuizGradeResponseDto> response = quizService.gradeQuiz(dto);
        return response;
    }
}
