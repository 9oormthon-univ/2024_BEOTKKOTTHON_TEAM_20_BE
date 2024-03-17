package com.beotkkot.qtudy.controller.auth;

import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.auth.KakaoUserInfo;
import com.beotkkot.qtudy.dto.auth.UserResponse;
import com.beotkkot.qtudy.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 프론트로부터 인가 code를 받아와 카카오 서버로부터 토큰 얻기
    @GetMapping("")
    public ResponseEntity<Object> kakaoLogin(@RequestParam("code") String code) {

        System.out.println("code = " + code);

        // 1. 인가 코드를 통해 카카오 서버로부터 토큰을 얻는다
        String accessToken = authService.getAccessToken(code);

        // 2. 발급받은 토큰을 이용해 사용자 정보를 조회
        KakaoUserInfo kakaoUserInfo = authService.getKakaoUserInfo(accessToken);

        // 3. 로그인
        Users user = authService.login(kakaoUserInfo);

        // 4. 유저 정보 리턴
        UserResponse userResponse = new UserResponse(
                "SU",
                "Sucess",
                user.getUserId(),
                user.getKakaoId(),
                user.getName(),
                user.getProfileImageUrl(),
                accessToken
        );
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(@RequestHeader("Authorization") String header) {

        System.out.println("header = " + header);

        String[] authHeader = header.split(" ");
        // 헤더에서 토큰 추출
        String accessToken = authHeader[1];

        // 토큰을 사용해 로그아웃 처리
        authService.logout(accessToken);

        // 로그아웃 성공 메시지 반환
        Map<String, String> response = new HashMap<>();
        response.put("code", "SU");
        response.put("message", "Success");
        return ResponseEntity.ok(response);
    }
}
