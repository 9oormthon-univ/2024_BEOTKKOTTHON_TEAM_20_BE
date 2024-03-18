package com.beotkkot.qtudy.controller.auth;

import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.object.KakaoUserInfo;
import com.beotkkot.qtudy.dto.response.auth.AuthResponseDto;
import com.beotkkot.qtudy.dto.response.auth.GetAuthResponseDto;
import com.beotkkot.qtudy.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 프론트로부터 인가 code를 받아와 카카오 서버로부터 토큰 얻기
    @GetMapping("")
    public ResponseEntity<? super GetAuthResponseDto> kakaoLogin(@RequestParam("code") String code) {

        System.out.println("code = " + code);

        // 1. 인가 코드를 통해 카카오 서버로부터 토큰을 얻는다
        String accessToken = authService.getAccessToken(code);
        if (accessToken.equals("")) {
            return GetAuthResponseDto.noAuthentication();
        }

        // 2. 발급받은 토큰을 이용해 사용자 정보를 조회
        KakaoUserInfo kakaoUserInfo = authService.getKakaoUserInfo(accessToken);
        if (kakaoUserInfo == null) {
            return GetAuthResponseDto.noExistUser();

        } else {
            // 3. 로그인
            Users user = authService.login(kakaoUserInfo);
            // 4. 유저 정보 리턴
            return GetAuthResponseDto.success(user, accessToken);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<? super AuthResponseDto> logout(@RequestHeader("Authorization") String accessToken) {

        // 헤더에서 토큰 추출
        System.out.println("accessToken = " + accessToken);

        // 토큰을 사용해 로그아웃 처리
        ResponseEntity<? super AuthResponseDto> response = authService.logout(accessToken);

        // 로그아웃 응답 반환
        return response;
    }
}
