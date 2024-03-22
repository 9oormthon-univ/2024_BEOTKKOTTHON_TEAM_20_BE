package com.beotkkot.qtudy.dto.object;

import com.beotkkot.qtudy.domain.comments.Comments;
import com.beotkkot.qtudy.domain.user.Users;
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
    private String name;
    private String profileImageUrl;

    public static CommentListItem of(Comments comment, Users user) {

        return CommentListItem.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
