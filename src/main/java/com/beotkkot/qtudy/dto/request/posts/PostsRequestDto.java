package com.beotkkot.qtudy.dto.request.posts;

import com.beotkkot.qtudy.domain.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
public class PostsRequestDto {
    private String title;
    private String content;
    private List<String> tag;
    private Long categoryId;

    public Posts toEntity(Long uid) {
        Date now = Date.from(Instant.now());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String writeDatetime = simpleDateFormat.format(now);
        String tagString = String.join(",", tag);

        return Posts.builder()
                .userUid(uid)
                .title(title)
                .content(content)
                .tag(tagString)
                .createdAt(writeDatetime)
                .commentCount(0)
                .scrapCount(0)
                .categoryId(categoryId)
                .build();
    }
}
