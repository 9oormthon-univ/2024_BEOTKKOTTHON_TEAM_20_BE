package com.beotkkot.qtudy.dto.response.mypage;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetMyPageInfoResponseDto extends ResponseDto {

    private String name;
    private String profileImageUrl;
    private String email;

    @Builder
    private GetMyPageInfoResponseDto(Users user, String email) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.name = user.getName();
        this.email = email;
        this.profileImageUrl = null;
    }

    public static ResponseEntity<GetMyPageInfoResponseDto> success(Users user, String email) {
        GetMyPageInfoResponseDto result = new GetMyPageInfoResponseDto(user, email);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noAuthentication() {
        ResponseDto result = new ResponseDto(ResponseCode.AUTHORIZATION_FAIL, ResponseMessage.AUTHORIZATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
