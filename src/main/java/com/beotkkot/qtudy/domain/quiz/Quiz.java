package com.beotkkot.qtudy.domain.quiz;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String tags;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(/*nullable = false,*/  columnDefinition = "TEXT")
    private String answer;

    @Column(/*nullable = false,*/  columnDefinition = "TEXT")
    private String explanation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String options;
}
