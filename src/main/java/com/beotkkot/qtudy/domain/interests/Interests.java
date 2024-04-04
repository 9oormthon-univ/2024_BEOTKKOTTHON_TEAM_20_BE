package com.beotkkot.qtudy.domain.interests;

import com.beotkkot.qtudy.domain.primaryKey.InterestsPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(InterestsPK.class) // 복합 키 생성
public class Interests {
    @Id
    private Long userId;

    @Id
    private Long categoryId;
}
