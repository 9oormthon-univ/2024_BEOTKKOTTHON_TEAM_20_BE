package com.beotkkot.qtudy.dto.response.quiz;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.dto.object.QuizListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetPostQuizResponseDto extends ResponseDto {
    private List<String> answerList;
    private List<Long> quizIdList;
    private List<QuizListItem> quizList;

    public GetPostQuizResponseDto(List<QuizListItem> QuizListItem, List<String> AnswerListItem, List<Long> quizIdList) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.answerList = AnswerListItem;
        this.quizIdList = quizIdList;
        this.quizList = QuizListItem;
    }

    public static ResponseEntity<GetPostQuizResponseDto> success(List<QuizListItem> QuizListItem, List<String> AnswerListItem, List<Long> quizIdList) {
        GetPostQuizResponseDto result = new GetPostQuizResponseDto(QuizListItem, AnswerListItem, quizIdList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedPost() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}