package com.beotkkot.qtudy.dto.response.comments;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.object.CommentListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@Getter
public class GetCommentsAllResponseDto extends ResponseDto {
    private int page;
    private List<CommentListItem> commentList;

    public GetCommentsAllResponseDto(List<CommentListItem> commentListItem, int page) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.page = page;
        this.commentList = commentListItem;
    }

    public static ResponseEntity<GetCommentsAllResponseDto> success(List<CommentListItem> commentListItem, int page) {
        GetCommentsAllResponseDto result = new GetCommentsAllResponseDto(commentListItem, page);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedPost(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}



