package com.beotkkot.qtudy.domain.comments;

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

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userUid;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String createdAt;
}
