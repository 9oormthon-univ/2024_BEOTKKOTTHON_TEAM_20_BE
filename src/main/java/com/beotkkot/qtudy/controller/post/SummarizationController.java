package com.beotkkot.qtudy.controller.post;

import com.beotkkot.qtudy.dto.response.posts.GetSummaryResponseDto;
import com.beotkkot.qtudy.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SummarizationController {

    private final PostsService postsService;

    /**
     * 사용자가 작성한 글에 대해 AI가 요약한 내용을 반환한다.
     */
    @GetMapping("/summary")
    public ResponseEntity<? super GetSummaryResponseDto> summary(@RequestParam("postId") Long postId) {
        // 포스트 아이디로부터 포스트 얻어오기
        ResponseEntity<? super GetSummaryResponseDto> response = postsService.getSummary(postId);
        return response;
    }
}
