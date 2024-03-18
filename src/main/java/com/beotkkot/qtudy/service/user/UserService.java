package com.beotkkot.qtudy.service.user;

import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.object.KakaoUserInfo;
import com.beotkkot.qtudy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    // 사용자 저장
    @Transactional
    public Long saveUser(KakaoUserInfo kakaoUserInfo) {

        // 사용자 이름 가져오기
        String name = kakaoUserInfo.getName();

        /**
         * 사용자 이름 랜덤 변경 (구현 필요) 
         */

        // User 엔티티 생성
        Users user = new Users();
        user.setName(name);
        user.setKakaoId(kakaoUserInfo.getId());
        user.setProfileImageUrl(kakaoUserInfo.getProfileImageUrl());

        // 유저 저장
        userRepository.save(user);

        // 아이디 반환
        return user.getUserId();
    }

    /**
     * 사용자 이름 변경 메서드 (구현 필요)
     */

    // 카카오 아이디로 회원 조회
    public Users findUserKaKaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId);
    }
}
