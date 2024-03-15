package com.beotkkot.qtudy.controller.auth;

import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.KakaoUserInfo;
import com.beotkkot.qtudy.dto.UserResponse;
import com.beotkkot.qtudy.service.auth.AuthService;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<Object> kakaoLogin(@RequestParam("code") String code, HttpSession session) {

        System.out.println("code = " + code);

        // 1. 인가 코드를 통해 카카오 서버로부터 토큰을 얻는다
        String accessToken = authService.getAccessToken(code);

        // 2. 발급받은 토큰을 이용해 사용자 정보를 조회
        KakaoUserInfo kakaoUserInfo = authService.getKakaoUserInfo(accessToken);

        // 3. 로그인
        Users user = authService.login(kakaoUserInfo);

        if (user.getKakaoId() != null) {
            session.setAttribute("kakaoId", user.getKakaoId());
            session.setAttribute("accessToken", accessToken);
        }

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
    public ResponseEntity<Map<String,String>> logout(HttpSession session) {

        String accessToken = (String)session.getAttribute("accessToken");
        if (accessToken != null && !"".equals(accessToken)) {
            authService.logout(accessToken);
            session.removeAttribute("kakaoId");
            session.removeAttribute("accessToken");
        }

        // 로그아웃 성공 메시지 반환
        Map<String, String> response = new HashMap<>();
        response.put("code", "SU");
        response.put("message", "Success");
        return ResponseEntity.ok(response);
    }
}
