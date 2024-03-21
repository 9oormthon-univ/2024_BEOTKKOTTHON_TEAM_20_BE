package com.beotkkot.qtudy.dto.response.mypage;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.object.PostListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@Getter
public class GetMyPageAllResponseDto extends ResponseDto {
    private int page;
    private int totalPages;
    private List<PostListItem> postList;

    public GetMyPageAllResponseDto(List<PostListItem> postListItem, int page, int totalPages) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.page = page;
        this.totalPages = totalPages;
        this.postList = postListItem;
    }

    public static ResponseEntity<GetMyPageAllResponseDto> success(List<PostListItem> postListItem, int page, int totalPages) {
        GetMyPageAllResponseDto result = new GetMyPageAllResponseDto(postListItem, page, totalPages);
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

    public static ResponseEntity<ResponseDto> noAuthentication() {
        ResponseDto result = new ResponseDto(ResponseCode.AUTHORIZATION_FAIL, ResponseMessage.AUTHORIZATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}



