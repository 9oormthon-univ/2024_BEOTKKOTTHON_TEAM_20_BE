package com.beotkkot.qtudy.service.auth;

import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.auth.KakaoUserInfo;
import com.beotkkot.qtudy.service.user.UserService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    // 1. 코드를 이용해 카카오로부터 토큰 얻기
    public String getAccessToken(String code) {

        String accessToken = "";
        String requestURL = "https://kauth.kakao.com/oauth/token";

        RestTemplate rt = new RestTemplate();

        // HttpHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id", "1aa00eb5cc4f6a4a7678fdbef90fbb97");
        params.add("redirect_uti", "http://localhost:3000/auth/redirected/kakao");
        params.add("code", code);

        // header, body를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(requestURL, HttpMethod.POST, tokenRequest, String.class);

        String responseBody = response.getBody();
        System.out.println("responseBody = " + responseBody);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(responseBody);
        accessToken = element.getAsJsonObject().get("access_token").getAsString();

        return accessToken;
    }

    // 2. 발급 받은 토큰으로 카카오 사용자 정보 확인
    public KakaoUserInfo getKakaoUserInfo(String accessToken) {

        String requestURL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate rt = new RestTemplate();

        // HttpHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // header를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(requestURL, HttpMethod.POST, kakaoUserInfoRequest, String.class);

        String responseBody = response.getBody();
        System.out.println("responseBody = " + responseBody);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(responseBody);

        // 유저 정보 추출
        Long id = element.getAsJsonObject().get("id").getAsLong();
        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
        System.out.println("properties = " + properties);
        String name = properties.getAsJsonObject().get("nickname").getAsString();
        String profileImageUrl = properties.getAsJsonObject().get("profile_image").getAsString();

        // 카카오 유저 정보 생성해서 리턴
        return new KakaoUserInfo(id, name, profileImageUrl, accessToken);
    }

    // 3. 사용자 정보를 DB에서 조회하고, 가입되지 않은 사용자라면 DB에 저장 후 해당 사용자 반환
    public Users login(KakaoUserInfo kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();   // 사용자의 카카오 아이디 불러오기
        Users findUser = userService.findUserKaKaoId(kakaoId); // 카카오 아이디로 유저 조회

        // 가입되지 않은 사용자라면
        if (findUser == null) {
            // DB에 새로 저장
            userService.saveUser(kakaoUserInfo);
            // 사용자 재조회
            findUser = userService.findUserKaKaoId(kakaoId);
        }

        return findUser;
    }

    public void logout(String accessToken) {
        String requestURL = "https://kapi.kakao.com/v1/user/logout";

        RestTemplate rt = new RestTemplate();

        // HttpHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        // header를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(requestURL, HttpMethod.POST, request, String.class);

        String responseBody = response.getBody();

    }
}