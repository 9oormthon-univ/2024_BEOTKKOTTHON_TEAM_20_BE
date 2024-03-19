package com.beotkkot.qtudy.dto.response.tags;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.object.TagListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetTop3TagsDto extends ResponseDto {
    private List<TagListItem> top3List;

    public GetTop3TagsDto(List<TagListItem> TagListItem) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.top3List = TagListItem;
    }

    public static ResponseEntity<GetTop3TagsDto> success(List<TagListItem> top3List) {
        GetTop3TagsDto result = new GetTop3TagsDto(top3List);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedPost(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
