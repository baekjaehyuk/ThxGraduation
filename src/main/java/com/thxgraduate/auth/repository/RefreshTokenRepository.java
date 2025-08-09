package com.thxgraduate.auth.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;


//In-memory refresh token store 서비스 확장시 수정 필요
@Repository
public class RefreshTokenRepository {

    // userId -> refreshToken (유저당 1개 정책)
    private final Map<String, String> store = new ConcurrentHashMap<>();

    /** 새 리프레시 토큰 저장 (기존 값이 있으면 덮어씀) */
    public void save(String userId, String refreshToken) {
        store.put(userId, refreshToken);
    }

    /** userId로 현재 refresh 토큰 조회 */
    public Optional<String> findByUserId(String userId) {
        return Optional.ofNullable(store.get(userId));
    }

    /** 주어진 refreshToken이 어떤 userId의 것인지 역조회 */
    public Optional<String> findUserIdByToken(String refreshToken) {
        return store.entrySet().stream()
            .filter(e -> refreshToken.equals(e.getValue()))
            .map(Map.Entry::getKey)
            .findFirst();
    }

    /** 해당 유저의 refresh 삭제 (로그아웃/회전 시 사용) */
    public void deleteByUserId(String userId) {
        store.remove(userId);
    }

    /** 주어진 토큰 삭제 (현재 세션 로그아웃/회전) */
    public void deleteByToken(String refreshToken) {
        findUserIdByToken(refreshToken).ifPresent(store::remove);
    }

    /** userId와 refreshToken 매칭 여부 확인 (재발급 검증 시 사용) */
    public boolean matches(String userId, String refreshToken) {
        return refreshToken != null && refreshToken.equals(store.get(userId));
    }
}
