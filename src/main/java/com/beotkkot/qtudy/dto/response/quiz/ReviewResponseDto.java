package com.beotkkot.qtudy.dto.response.quiz;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.object.QuizGradeListItem;
import com.beotkkot.qtudy.dto.object.ReviewListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class ReviewResponseDto extends ResponseDto{
    private int page;
    private int totalPages;
    private List<ReviewListItem> reviewListItems;

    public ReviewResponseDto(List<ReviewListItem> reviewListItems, int page, int totalPages) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.page = page;
        this.totalPages = totalPages;
        this.reviewListItems = reviewListItems;
    }

    public static ResponseEntity<ReviewResponseDto> success(List<ReviewListItem> reviewListItems, int page, int totalPages) {
        ReviewResponseDto result = new ReviewResponseDto(reviewListItems, page, totalPages);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedPost() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
