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

    @GetMapping("/posts/all")
    public ResponseEntity<? super GetPostsAllResponseDto> getAllPost(@RequestParam("page")int page) {
        ResponseEntity<? super GetPostsAllResponseDto> response = postsService.getAllPost(page);
        return response;
    }

    @PostMapping("/posts")
    public ResponseEntity<? super PostsResponseDto> save(@RequestHeader(value="Authorization") String token, @RequestBody PostsRequestDto requestDto) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return PostsResponseDto.databaseError();
                // return PostsResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return PostsResponseDto.databaseError();
        }

        ResponseEntity<? super PostsResponseDto> response = postsService.savePost(kakao_uid, requestDto);
        return response;
    }

    @GetMapping("/posts")
    public ResponseEntity<? super GetPostsResponseDto> getPost(@RequestParam("postId") Long postId) {
        ResponseEntity<? super GetPostsResponseDto> response = postsService.getPost(postId);
        return response;
    }

    @PatchMapping("/posts")
    public ResponseEntity<? super PostsResponseDto> patchPost(@RequestParam("postId") Long postId, @RequestHeader(value="Authorization") String token, @RequestBody PostsRequestDto requestDto) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return PostsResponseDto.databaseError();
                // return PostsResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return PostsResponseDto.databaseError();
        }

        ResponseEntity<? super PostsResponseDto> response = postsService.patchPost(postId, kakao_uid, requestDto);
        return response;
    }

    @DeleteMapping("/posts")
    public ResponseEntity<? super PostsResponseDto> deletePost(@RequestParam("postId") Long postId, @RequestHeader(value="Authorization") String token) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return PostsResponseDto.databaseError();
                // return PostsResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return PostsResponseDto.databaseError();
        }
        ResponseEntity<? super PostsResponseDto> response = postsService.deletePost(postId, kakao_uid);
        return response;
    }

    @GetMapping("/posts/my-post-list")
    public ResponseEntity<? super GetPostsAllResponseDto> getMyPost(@RequestHeader(value="Authorization") String token, @RequestParam("page")int page) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return PostsResponseDto.databaseError();
                // return PostsResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return PostsResponseDto.databaseError();
        }
        ResponseEntity<? super GetPostsAllResponseDto> response = postsService.getMyPost(kakao_uid, page);
        return response;
    }

    @GetMapping("/posts/search-list")
    public ResponseEntity<? super GetPostsAllResponseDto> getSearchPost(@RequestParam("searchWord") String searchWord, @RequestParam("page")int page) {
        ResponseEntity<? super GetPostsAllResponseDto> response = postsService.getSearchPost(searchWord, page);
        return response;
    }

    @GetMapping("/posts/category-list")
    public ResponseEntity<? super GetPostsAllResponseDto> searchPostsByCategory(@RequestParam("categoryId") List<Long> categories, @RequestParam("page")int page) {
        ResponseEntity<? super GetPostsAllResponseDto> response = postsService.getCategorySearchPost(categories, page);
        return response;
    }

    // 스크랩
    @PutMapping("/posts/scrap")
    public ResponseEntity<? super PutScrapResponseDto> putScrap(@RequestParam("postId") Long postId, @RequestHeader(value="Authorization") String token) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return PostsResponseDto.databaseError();
                // return PostsResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return PostsResponseDto.databaseError();
        }
        ResponseEntity<? super PutScrapResponseDto> response = postsService.putScrap(postId, kakao_uid);
        return response;
    }

    @GetMapping("/posts/scrap-list")
    public ResponseEntity<? super GetPostsAllResponseDto> getAllScrapPost(@RequestHeader(value="Authorization") String token, @RequestParam("page")int page) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return PostsResponseDto.databaseError();
                // return PostsResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return PostsResponseDto.databaseError();
        }
        ResponseEntity<? super GetPostsAllResponseDto> response = postsService.getAllScrapPost(kakao_uid, page);
        return response;
    }
}
