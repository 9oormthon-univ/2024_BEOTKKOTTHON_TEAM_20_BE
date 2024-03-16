package com.beotkkot.qtudy.dto.response.posts;

import com.beotkkot.qtudy.common.ResponseCode;
import com.beotkkot.qtudy.common.ResponseMessage;
import com.beotkkot.qtudy.domain.posts.Posts;
import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@Getter
public class GetPostsResponseDto extends ResponseDto {
    private Long postId;
    private String title;
    private String content;
    private int scrapCount;
    private int commentCount;
    private List<String> tag;
    private Long categoryId;
    private String userNickname;
    private String userProfileImage;
    private String createdAt;

    @Builder
    private GetPostsResponseDto(Posts post, Users user) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        List<String> tag = Arrays.asList(post.getTag().split("\\s*,\\s*"));

        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.commentCount = post.getCommentCount();
        this.scrapCount = post.getScrapCount();
        this.tag = tag;
        this.categoryId = post.getCategoryId();
        this.userNickname = user.getName();
        this.userProfileImage = user.getProfileImageUrl();
        this.createdAt = post.getCreatedAt();
    }

    public static ResponseEntity<GetPostsResponseDto> success(Posts post, Users user) {

        GetPostsResponseDto result = new GetPostsResponseDto(post, user);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistPost() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
