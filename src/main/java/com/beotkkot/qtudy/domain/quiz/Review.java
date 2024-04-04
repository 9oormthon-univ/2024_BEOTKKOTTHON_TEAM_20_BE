package com.beotkkot.qtudy.domain.quiz;

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

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long quizId;

    @Column(nullable = false)
    private int userAnswer;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private boolean correct;

    private String explanation;

    private Long categoryId;

    private String type;

    private String tags;

    private int score;

    private String createdAt;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reviewId;
}
