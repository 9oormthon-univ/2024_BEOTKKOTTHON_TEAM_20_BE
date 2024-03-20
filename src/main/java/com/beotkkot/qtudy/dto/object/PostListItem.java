package com.beotkkot.qtudy.dto.object;

import com.beotkkot.qtudy.domain.posts.Posts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostListItem {
    private Long postId;
    private String title;
    private String summary;
    private int scrapCount;
    private int commentCount;
    private Long categoryId;
    private List<String> tag;
    private String createdAt;

    public static PostListItem of(Posts post) {
        List<String> tag = Arrays.asList(post.getTag().split("\\s*,\\s*"));

        return PostListItem.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .summary(post.getSummary())
                .scrapCount(post.getScrapCount())
                .commentCount(post.getCommentCount())
                .tag(tag)
                .categoryId(post.getCategoryId())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
