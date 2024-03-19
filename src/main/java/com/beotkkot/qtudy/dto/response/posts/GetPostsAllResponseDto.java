package com.beotkkot.qtudy.dto.response.posts;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.object.PostListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@Getter
public class GetPostsAllResponseDto extends ResponseDto {
    private int page;
    private List<PostListItem> postList;

    public GetPostsAllResponseDto(List<PostListItem> PostListItem, int page) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.page = page;
        this.postList = PostListItem;
    }

    public static ResponseEntity<GetPostsAllResponseDto> success(List<PostListItem> PostListItem, int page) {
        GetPostsAllResponseDto result = new GetPostsAllResponseDto(PostListItem, page);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedPost(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}



