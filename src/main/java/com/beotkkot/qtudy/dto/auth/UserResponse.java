package com.beotkkot.qtudy.dto.auth;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String code;
    private String message;
    private Long id;
    private Long kakaoId;
    private String name;
    private String profileImageUrl;
    private String accessToken;

}
