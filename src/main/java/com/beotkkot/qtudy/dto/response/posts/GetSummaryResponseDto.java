package com.beotkkot.qtudy.dto.response.posts;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetSummaryResponseDto extends ResponseDto {
    private Long postId;
    private String summary;

    @Builder
    private GetSummaryResponseDto(Long postId, String summary) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);

        this.postId = postId;
        this.summary = summary;
    }

    public static ResponseEntity<GetSummaryResponseDto> success(Long postId, String summary) {

        GetSummaryResponseDto result = new GetSummaryResponseDto(postId, summary);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistPost() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
