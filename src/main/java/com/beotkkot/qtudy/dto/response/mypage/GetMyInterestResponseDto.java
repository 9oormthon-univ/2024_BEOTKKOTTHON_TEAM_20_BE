package com.beotkkot.qtudy.dto.response.mypage;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetMyInterestResponseDto extends ResponseDto {

    private List<Long> interests;

    public GetMyInterestResponseDto(List<Long> interests) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.interests = interests;
    }

    public static ResponseEntity<GetMyInterestResponseDto> success(List<Long> interestIds) {
        GetMyInterestResponseDto result = new GetMyInterestResponseDto(interestIds);
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
