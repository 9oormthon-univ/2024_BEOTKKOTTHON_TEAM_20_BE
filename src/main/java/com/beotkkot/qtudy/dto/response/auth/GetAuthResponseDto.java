package com.beotkkot.qtudy.dto.response.auth;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetAuthResponseDto extends ResponseDto {

    private Long kakaoId;
    private Long id;
    private String accessToken;
    private String name;
    private String profileImageUrl;

    @Builder
    private GetAuthResponseDto(Users user, String accessToken) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);

        this.kakaoId = user.getKakaoId();
        this.id = user.getUserId();
        this.accessToken = accessToken;
        this.name = user.getName();
        this.profileImageUrl = user.getProfileImageUrl();
    }

    public static ResponseEntity<GetAuthResponseDto> success(Users user, String accessToken) {
        GetAuthResponseDto result = new GetAuthResponseDto(user, accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noAuthentication() {
        ResponseDto result = new ResponseDto(ResponseCode.AUTHORIZATION_FAIL, ResponseMessage.AUTHORIZATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
