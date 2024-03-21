package com.beotkkot.qtudy.dto.response.quiz;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.domain.posts.Posts;
import com.beotkkot.qtudy.domain.quiz.Quiz;
import com.beotkkot.qtudy.domain.quiz.Review;
import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.object.QuizListItem;
import com.beotkkot.qtudy.dto.object.ReviewDetailListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import com.beotkkot.qtudy.dto.response.posts.GetPostsResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@Getter
public class GetReviewResponseDto extends ResponseDto {
    private int totalScore;
    private List<ReviewDetailListItem> reviewList;

    public GetReviewResponseDto(List<ReviewDetailListItem> reviewList, int totalScore) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.totalScore = totalScore;
        this.reviewList = reviewList;
    }

    public static ResponseEntity<GetReviewResponseDto> success(List<ReviewDetailListItem> reviewList, int totalScore) {
        GetReviewResponseDto result = new GetReviewResponseDto(reviewList, totalScore);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedPost() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
