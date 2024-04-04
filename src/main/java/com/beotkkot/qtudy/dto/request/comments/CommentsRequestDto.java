package com.beotkkot.qtudy.dto.request.comments;

import com.beotkkot.qtudy.domain.comments.Comments;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@Getter
public class CommentsRequestDto {
    private String content;

    public Comments toEntity(Long postId, Long userUid) {
        Date now = Date.from(Instant.now());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String writeDatetime = simpleDateFormat.format(now);

        return Comments.builder()
                .userUid(userUid)
                .postId(postId)
                .content(content)
                .createdAt(writeDatetime)
                .build();
    }

}
