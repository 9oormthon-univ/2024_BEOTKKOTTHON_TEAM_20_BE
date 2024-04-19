package com.beotkkot.qtudy.domain.tags;

import com.beotkkot.qtudy.domain.category.Category;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void increaseTagCount() {
        this.count++;
    }

    public void decreaseTagCount() {
        this.count--;
    }
}
