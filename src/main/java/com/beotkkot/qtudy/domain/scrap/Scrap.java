package com.beotkkot.qtudy.domain.scrap;

import com.beotkkot.qtudy.domain.primaryKey.ScrapPk;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ScrapPk.class) // 복합 키 생성
public class Scrap {
    @Id
    private Long userId;

    @Id
    private Long postId;

    private String scrapAt;
}
