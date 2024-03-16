package com.beotkkot.qtudy.controller.post;

import com.beotkkot.qtudy.dto.request.posts.PostsRequestDto;
import com.beotkkot.qtudy.dto.response.posts.GetPostsAllResponseDto;
import com.beotkkot.qtudy.dto.response.posts.GetPostsResponseDto;
import com.beotkkot.qtudy.dto.response.posts.PostsResponseDto;
import com.beotkkot.qtudy.dto.response.posts.PutScrapResponseDto;
import com.beotkkot.qtudy.service.posts.PostsService;
import com.beotkkot.qtudy.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostsService postsService;
    private final AuthService authService;

    @GetMapping("/posts")
    public ResponseEntity<? super GetPostsAllResponseDto> getAllPost() {
        ResponseEntity<? super GetPostsAllResponseDto> response = postsService.getAllPost();
        return response;
    }

    @PostMapping("/posts")
    public ResponseEntity<? super PostsResponseDto> save(@RequestHeader(value="Authorization") String token, @RequestBody PostsRequestDto requestDto) {
        Long kakao_uid = authService.getKakaoUserInfo(token).getId();

        ResponseEntity<? super PostsResponseDto> response = postsService.savePost(kakao_uid, requestDto);
        return response;
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<? super GetPostsResponseDto> getPost(@PathVariable Long postId) {
        ResponseEntity<? super GetPostsResponseDto> response = postsService.getPost(postId);
        return response;
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<? super PostsResponseDto> patchPost(@PathVariable Long postId, @RequestHeader(value="Authorization") String token, @RequestBody PostsRequestDto requestDto) {
        Long kakao_uid = authService.getKakaoUserInfo(token).getId();

        ResponseEntity<? super PostsResponseDto> response = postsService.patchPost(postId, kakao_uid, requestDto);
        return response;
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<? super PostsResponseDto> deletePost(@PathVariable Long postId, @RequestHeader(value="Authorization") String token) {
        Long kakao_uid = authService.getKakaoUserInfo(token).getId();
        ResponseEntity<? super PostsResponseDto> response = postsService.deletePost(postId, kakao_uid);
        return response;
    }

    @GetMapping("/posts/my-post-list")
    public ResponseEntity<? super GetPostsAllResponseDto> getMyPost(@RequestHeader(value="Authorization") String token) {
        Long kakao_uid = authService.getKakaoUserInfo(token).getId();
        ResponseEntity<? super GetPostsAllResponseDto> response = postsService.getMyPost(kakao_uid);
        return response;
    }

    @GetMapping("/posts/search-list/{searchWord}")
    public ResponseEntity<? super GetPostsAllResponseDto> getSearchPost(@PathVariable String searchWord) {
        ResponseEntity<? super GetPostsAllResponseDto> response = postsService.getSearchPost(searchWord);
        return response;
    }

    @GetMapping("/posts/category-list")
    public ResponseEntity<? super GetPostsAllResponseDto> searchPostsByCategory(@RequestParam("category") List<Long> categories) {
        ResponseEntity<? super GetPostsAllResponseDto> response = postsService.getCategorySearchPost(categories);
        return response;
    }

    // 스크랩
    @PutMapping("/posts/{postId}/scrap")
    public ResponseEntity<? super PutScrapResponseDto> putScrap(@PathVariable("postId") Long postId, @RequestHeader(value="Authorization") String token) {
        Long kakao_uid = authService.getKakaoUserInfo(token).getId();
        ResponseEntity<? super PutScrapResponseDto> response = postsService.putScrap(postId, kakao_uid);
        return response;
    }

    @GetMapping("/posts/scrap-list")
    public ResponseEntity<? super GetPostsAllResponseDto> getAllScrapPost(@RequestHeader(value="Authorization") String token) {
        Long kakao_uid = authService.getKakaoUserInfo(token).getId();
        ResponseEntity<? super GetPostsAllResponseDto> response = postsService.getAllScrapPost(kakao_uid);
        return response;
    }
}
