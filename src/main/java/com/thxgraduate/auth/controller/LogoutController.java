package com.thxgraduate.auth.controller;

import com.thxgraduate.auth.repository.RefreshTokenRepository;
import com.thxgraduate.common.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LogoutController {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Value("${app.cookie.secure:true}")
    private boolean cookieSecure;

    @Value("${app.cookie.same-site:None}")
    private String cookieSameSite;

    @Value("${app.cookie.domain:}")
    private String cookieDomain;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        // 1) 서버측 무효화
        if (refreshToken != null) {
            refreshTokenRepository.deleteByToken(refreshToken);
        }

        // 2) 클라이언트 쿠키 삭제
        ResponseCookie clearAccess = buildClearingCookie("accessToken");
        ResponseCookie clearRefresh = buildClearingCookie("refreshToken");

        response.addHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());

        return ResponseEntity.ok().build();
    }

    private ResponseCookie buildClearingCookie(String name) {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from(name, "")
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite(cookieSameSite)
            .path("/")
            .maxAge(0);
        if (cookieDomain != null && !cookieDomain.isBlank()) {
            b = b.domain(cookieDomain);
        }
        return b.build();
    }
}