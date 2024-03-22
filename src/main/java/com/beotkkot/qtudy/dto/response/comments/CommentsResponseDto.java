package com.beotkkot.qtudy.dto.response.comments;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CommentsResponseDto extends ResponseDto{

    private String name;
    private String profileImageUrl;

    public CommentsResponseDto(String name, String profileImageUrl) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public static ResponseEntity<CommentsResponseDto> success(String name, String profileImageUrl) {
        CommentsResponseDto result = new CommentsResponseDto(name, profileImageUrl);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedPost(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<? super CommentsResponseDto> notExistedComment() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_COMMENT, ResponseMessage.NOT_EXISTED_COMMENT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> noPermission() {
        ResponseDto result = new ResponseDto(ResponseCode.NO_PERMISSION, ResponseMessage.NO_PERMISSION);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }
}
