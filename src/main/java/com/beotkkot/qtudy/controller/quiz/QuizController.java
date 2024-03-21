package com.beotkkot.qtudy.controller.quiz;

import com.beotkkot.qtudy.dto.request.quiz.GenerateQuizRequestDto;
import com.beotkkot.qtudy.dto.request.quiz.GradeQuizRequestDto;
import com.beotkkot.qtudy.dto.response.posts.PostsResponseDto;
import com.beotkkot.qtudy.dto.response.quiz.GetPostQuizResponseDto;
import com.beotkkot.qtudy.dto.response.quiz.GetReviewResponseDto;
import com.beotkkot.qtudy.dto.response.quiz.QuizGradeResponseDto;
import com.beotkkot.qtudy.dto.response.quiz.ReviewResponseDto;
import com.beotkkot.qtudy.service.auth.AuthService;
import com.beotkkot.qtudy.service.quiz.QuizService;
import com.beotkkot.qtudy.service.quiz.ReviewService;
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
    private final AuthService authService;
    private final ReviewService reviewService;

//     퀴즈 생성 및 저장
    @PostMapping("/quiz")
    public ResponseEntity<?> generateQuiz(@RequestBody GenerateQuizRequestDto dto) {
        try {
            return ResponseEntity.ok().body(quizService.generateQuiz(dto));
        } catch (HttpClientErrorException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 태그별 랜덤 10개 퀴즈 출력
    @GetMapping("/quiz/tag-quiz")
    public ResponseEntity<? super GetPostQuizResponseDto> getTagQuiz(@RequestParam String tagName) {
        ResponseEntity<? super GetPostQuizResponseDto> response = quizService.getTagQuiz(tagName);
        return response;
    }

    // 게시글 별 생성된 퀴즈 출력
    @GetMapping("/quiz/post-quiz")
    public ResponseEntity<? super GetPostQuizResponseDto> getPostQuiz(@RequestParam Long postId) {
        ResponseEntity<? super GetPostQuizResponseDto> response = quizService.getPostQuiz(postId);
        return response;
    }


    // 정답 채점
    @PostMapping("/quiz/grade")
    public ResponseEntity<? super QuizGradeResponseDto> gradeQuiz(@RequestHeader(value="Authorization") String token, @RequestBody GradeQuizRequestDto dto) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return PostsResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return PostsResponseDto.databaseError();
        }
        ResponseEntity<? super QuizGradeResponseDto> response = quizService.gradeQuiz(dto, kakao_uid);
        return response;
    }

    // 내가 푼 퀴즈 전체 조회
    @GetMapping("/my/quiz/all")
    public ResponseEntity<? super ReviewResponseDto> getMyQuizList(@RequestHeader(value="Authorization") String token, @RequestParam int page) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return PostsResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return PostsResponseDto.databaseError();
        }
        ResponseEntity<? super ReviewResponseDto> response = reviewService.getMyQuizList(kakao_uid, page);
        return response;
    }

    // 내가 푼 퀴즈 상세 조회
    @GetMapping("/my/quiz")
    public ResponseEntity<? super GetReviewResponseDto> getMyQuiz(@RequestHeader(value="Authorization") String token, @RequestParam String reviewId) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return PostsResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return PostsResponseDto.databaseError();
        }
        ResponseEntity<? super GetReviewResponseDto> response = reviewService.getMyQuiz(kakao_uid, reviewId);
        return response;
    }
}
