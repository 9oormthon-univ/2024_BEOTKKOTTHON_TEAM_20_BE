package com.beotkkot.qtudy.dto.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfo {
    private Long id;
    private String name;
    private String profileImageUrl;
    private String accessToken;

}
