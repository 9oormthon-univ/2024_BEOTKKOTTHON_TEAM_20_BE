package com.beotkkot.qtudy.service.auth;

import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.object.KakaoUserInfo;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import com.beotkkot.qtudy.dto.response.auth.AuthResponseDto;
import com.beotkkot.qtudy.service.user.UserService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    @Value("${OAUTH_CLIENT_ID}")
    private String OAUTH_CLIENT_ID;
    private final UserService userService;

    // 1. 코드를 이용해 카카오로부터 토큰 얻기
    public String getAccessToken(String code) {

        String accessToken = "";
        String requestURL = "https://kauth.kakao.com/oauth/token";

        ResponseEntity<String> response;

        RestTemplate rt = new RestTemplate();

        // HttpHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id", OAUTH_CLIENT_ID);
        params.add("redirect_uti", "https://qtudy.site/auth/redirected/kakao");
        params.add("code", code);

        // header, body를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        try {
            response = rt.exchange(requestURL, HttpMethod.POST, tokenRequest, String.class);
            String responseBody = response.getBody();
            System.out.println("responseBody = " + responseBody);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(responseBody);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();

        } catch (Exception exception) {
            log.info("error message: " + exception.getMessage());
            return accessToken;
        }

        return accessToken;
    }

    // 2. 발급 받은 토큰으로 카카오 사용자 정보 확인
    public KakaoUserInfo getKakaoUserInfo(String accessToken) {

        String requestURL = "https://kapi.kakao.com/v2/user/me";
        ResponseEntity<String> response;

        RestTemplate rt = new RestTemplate();

        // HttpHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // header를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        try {
            response = rt.exchange(requestURL, HttpMethod.POST, kakaoUserInfoRequest, String.class);

            String responseBody = response.getBody();
            System.out.println("responseBody = " + responseBody);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(responseBody);

            // 유저 정보 추출
            Long id = element.getAsJsonObject().get("id").getAsLong();
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            System.out.println("properties = " + properties);
            String name = properties.getAsJsonObject().get("nickname").getAsString();
            String profileImageUrl = properties.getAsJsonObject().get("profile_image").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
            // 카카오 유저 정보 생성해서 리턴
            return new KakaoUserInfo(id, name, email, profileImageUrl, accessToken);

        } catch (Exception exception) {
            log.info("error message: " + exception.getMessage());
            return null;
        }
    }

    @Transactional
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
            // 최초 사용자로 설정
            findUser.setFirst(true);
            System.out.println("findUser.isFirst() = " + findUser.isFirst());
        } else {    // 이미 가입한 사용자라면
            if (findUser.isFirst()) {
                findUser.setFirst(false);
            }
        }
        return findUser;
    }

    public ResponseEntity<? super AuthResponseDto> logout(String accessToken) {
        String requestURL = "https://kapi.kakao.com/v1/user/logout";

        ResponseEntity<String> response;

        RestTemplate rt = new RestTemplate();

        // HttpHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        // header를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        try {
            response = rt.exchange(requestURL, HttpMethod.POST, request, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException exception) {
            // 4xx 에러 (클라이언트 오류) 처리
            log.info("error message: " + exception.getMessage());
            return AuthResponseDto.noAuthentication();
        } catch (HttpServerErrorException exception) {
            // 5xx 에러 (서버 오류) 처리
            log.info("error message: " + exception.getMessage());
            return ResponseDto.databaseError();
        } catch (Exception exception) {
            // 그 외 예외 처리
            log.info("error message: " + exception.getMessage());
        }
        return AuthResponseDto.success();
    }
}
