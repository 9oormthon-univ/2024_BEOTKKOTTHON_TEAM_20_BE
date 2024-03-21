package com.beotkkot.qtudy.controller.mypage;

import com.beotkkot.qtudy.dto.response.mypage.GetMyInterestResponseDto;
import com.beotkkot.qtudy.dto.response.mypage.GetMyPageAllResponseDto;
import com.beotkkot.qtudy.dto.response.mypage.GetMyPageInfoResponseDto;
import com.beotkkot.qtudy.dto.response.mypage.MyInterestResponseDto;
import com.beotkkot.qtudy.service.auth.AuthService;
import com.beotkkot.qtudy.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final AuthService authService;
    private final MyPageService myPageService;

    // 관심 분야 목록 초기 선택
    @PostMapping("/my/interests")
    public ResponseEntity<? super MyInterestResponseDto> saveMyInterests(@RequestParam("interests") List<Long> interests, @RequestHeader("Authorization") String token) {

        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return MyInterestResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return MyInterestResponseDto.databaseError();
        }

        ResponseEntity<? super MyInterestResponseDto> response = myPageService.saveMyInterests(kakao_uid, interests);
        return response;
    }

    // 내 관심 분야 목록 조회
    @GetMapping("my/interests")
    public ResponseEntity<? super GetMyInterestResponseDto> getMyInterests(@RequestHeader("Authorization") String token) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return GetMyInterestResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return GetMyInterestResponseDto.databaseError();
        }
        ResponseEntity<? super GetMyInterestResponseDto> response = myPageService.getMyInterests(kakao_uid);
        return response;
    }

    // 내 관심 분야 목록 수정
    @PatchMapping("my/interests")
    public ResponseEntity<? super MyInterestResponseDto> patchMyInterests(@RequestParam("interests") List<Long> interests, @RequestHeader("Authorization") String token) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return MyInterestResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return MyInterestResponseDto.databaseError();
        }
        ResponseEntity<? super MyInterestResponseDto> response = myPageService.patchMyInterests(kakao_uid, interests);
        return response;
    }

    // 사용자 프로필 조회
    @GetMapping("/my")
    public ResponseEntity<? super GetMyPageInfoResponseDto> getMyPageInfo(@RequestHeader("Authorization") String token) {

        Long kakao_uid;
        String email;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return GetMyPageInfoResponseDto.noAuthentication();
            email = authService.getKakaoUserInfo(token).getEmail();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return GetMyPageInfoResponseDto.databaseError();
        }

        ResponseEntity<? super GetMyPageInfoResponseDto> response = myPageService.getMyPageInfo(kakao_uid, email);

        return response;
    }

    // 내가 작성한 게시글 확인
    @GetMapping("my/posts")
    public ResponseEntity<? super GetMyPageAllResponseDto> getAllPost(@RequestParam("page") int page, @RequestHeader("Authorization") String token) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return GetMyPageAllResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return GetMyPageAllResponseDto.databaseError();
        }

        ResponseEntity<? super GetMyPageAllResponseDto> response = myPageService.getAllPost(kakao_uid, page);
        return response;
    }

    // 내가 스크랩한 글 확인
    @GetMapping("/my/scrap")
    public ResponseEntity<? super GetMyPageAllResponseDto> getAllScrapPost(@RequestParam("page") int page, @RequestHeader("Authorization") String token) {
        Long kakao_uid;
        try {
            kakao_uid = authService.getKakaoUserInfo(token).getId();
            if (kakao_uid == null)
                return GetMyPageAllResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return GetMyPageAllResponseDto.databaseError();
        }

        ResponseEntity<? super GetMyPageAllResponseDto> response = myPageService.getAllScrapPost(kakao_uid, page);
        return response;
    }
}
