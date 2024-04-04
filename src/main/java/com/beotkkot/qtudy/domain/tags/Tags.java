package com.beotkkot.qtudy.domain.tags;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false)
    private String name;

    private int count; // 태그 언급 횟수

    private Long categoryId;

    public void increaseTagCount() {
        this.count++;
    }

    public void decreaseTagCount() {
        this.count--;
    }
}
