package com.beotkkot.qtudy.dto.response.posts;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PutScrapResponseDto extends ResponseDto {

    private PutScrapResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PutScrapResponseDto> success() {
        PutScrapResponseDto result = new PutScrapResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedPost() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> noPermission() {
        ResponseDto result = new ResponseDto(ResponseCode.NO_PERMISSION, ResponseMessage.NO_PERMISSION);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }
}
