package com.beotkkot.qtudy.dto.object;

import com.beotkkot.qtudy.domain.comments.Comments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentListItem {
    private Long commentId;
    private String content;
    private String createdAt;

    public static CommentListItem of(Comments comment) {

        return CommentListItem.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
