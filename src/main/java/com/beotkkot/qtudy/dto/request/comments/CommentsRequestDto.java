package com.beotkkot.qtudy.dto.request.comments;

import com.beotkkot.qtudy.domain.comments.Comments;
import com.beotkkot.qtudy.domain.posts.Posts;
import com.beotkkot.qtudy.domain.user.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@Getter
public class CommentsRequestDto {
    private String content;

    public Comments toEntity(Posts post, Users user) {
        Date now = Date.from(Instant.now());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String writeDatetime = simpleDateFormat.format(now);

        return Comments.builder()
                .user(user)
                .post(post)
                .content(content)
                .createdAt(writeDatetime)
                .build();
    }
}
