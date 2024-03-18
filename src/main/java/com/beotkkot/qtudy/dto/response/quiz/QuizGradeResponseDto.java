package com.beotkkot.qtudy.dto.response.quiz;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.object.QuizGradeListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class QuizGradeResponseDto extends ResponseDto {
    private int score;
    private int total_score;
    private List<QuizGradeListItem> gradeList;

    public QuizGradeResponseDto(List<QuizGradeListItem> gradeList, int score) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.score = score;
        this.total_score = 100;
        this.gradeList = gradeList;
    }

    public static ResponseEntity<QuizGradeResponseDto> success(List<QuizGradeListItem> gradeList, int score) {
        QuizGradeResponseDto result = new QuizGradeResponseDto(gradeList, score);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedPost() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
