package com.beotkkot.qtudy.domain.posts;

import com.beotkkot.qtudy.dto.request.posts.PostsRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;


@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Posts {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private Long userUid;

    @Column(nullable = false)
    private String title;

    private String tag;

    private Long categoryId;

    @Column(columnDefinition = "TEXT")
    private String content;

    // AI 요약본
    @Column(columnDefinition = "TEXT")
    private String aiScript;

    private int commentCount;

    private int scrapCount;

    private String createdAt;

    private String updatedAt;

    public void patchPost(PostsRequestDto dto) {
        Date now = Date.from(Instant.now());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateDatetime = simpleDateFormat.format(now);
        String tagString = String.join(",", dto.getTag());

        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.updatedAt = updateDatetime;
        this.tag = tagString;
        this.categoryId = dto.getCategoryId();
    }

    public void increaseScrapCount() {
        this.scrapCount++;
    }

    public void decreaseScrapCount() {
        this.scrapCount--;
    }
}
