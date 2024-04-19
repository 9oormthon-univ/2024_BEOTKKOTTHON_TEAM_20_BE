package com.beotkkot.qtudy.domain.comments;

import com.beotkkot.qtudy.domain.posts.Posts;
import com.beotkkot.qtudy.domain.user.Users;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String createdAt;

    public void updateContent(String content) {
        this.content = content;
    }
}
