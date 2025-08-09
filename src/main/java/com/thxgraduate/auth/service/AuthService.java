package com.thxgraduate.auth.service;

import com.thxgraduate.auth.controller.dto.KakaoUserInfoDto;
import com.thxgraduate.auth.controller.dto.TokenPair;
import com.thxgraduate.auth.repository.RefreshTokenRepository;
import com.thxgraduate.auth.repository.UserRepository;
import com.thxgraduate.common.jwt.JwtProvider;
import com.thxgraduate.auth.entity.User;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public User loginOrRegister(KakaoUserInfoDto kakaoUser) {
        return userRepository.findBySocialId(kakaoUser.getId())
                .orElseGet(() -> userRepository.save(User.builder()
                        .socialId(kakaoUser.getId())
                        .nickName(kakaoUser.getNickname())
                        .link(UUID.randomUUID())
                        .build()));
    }

    public TokenPair issueTokens(User user) {
        String access = jwtProvider.createAccessToken(user.getId().toString());
        String refresh = jwtProvider.createRefreshToken(user.getId().toString());
        refreshTokenRepository.save(user.getId().toString(), refresh);
        return new TokenPair(access, refresh);
    }

    @Transactional(readOnly = true)
    public User getById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}