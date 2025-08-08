package com.thxgraduate.auth.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

    private final Map<String, String> store = new ConcurrentHashMap<>();

    public void save(String userId, String refreshToken) {
        store.put(userId, refreshToken);
    }
}
