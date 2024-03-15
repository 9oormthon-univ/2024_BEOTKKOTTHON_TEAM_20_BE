package com.beotkkot.qtudy.repository.user;

import com.beotkkot.qtudy.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByKakaoId(Long kakaoId);

}
