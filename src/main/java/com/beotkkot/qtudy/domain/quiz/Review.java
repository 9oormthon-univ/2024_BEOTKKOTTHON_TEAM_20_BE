package com.beotkkot.qtudy.domain.quiz;

import com.beotkkot.qtudy.domain.user.Users;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(nullable = false)
    private int userAnswer;

    @Column(nullable = false)
    private boolean correct;

    private Long categoryId;

    private String type;

    private int score;

    private String createdAt;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reviewId;
}
