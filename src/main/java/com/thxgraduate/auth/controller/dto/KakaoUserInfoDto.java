package com.thxgraduate.auth.controller.dto;

import java.util.Map;
import lombok.Getter;

@Getter
public class KakaoUserInfoDto {
    private final String id;
    private final String nickname;

    public KakaoUserInfoDto(Map attributes) {
        this.id = String.valueOf(attributes.get("id"));
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        this.nickname = (String) profile.get("nickname");
    }
}