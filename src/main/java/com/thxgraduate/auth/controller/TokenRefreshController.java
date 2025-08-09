package com.thxgraduate.auth.controller;

import com.thxgraduate.auth.controller.dto.TokenPair;
import com.thxgraduate.auth.entity.User;
import com.thxgraduate.auth.service.AuthService;
import com.thxgraduate.common.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth/token")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "토큰 재발급 API")
public class TokenRefreshController {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "쿠키의 refreshToken으로 access/refresh 재발급")
    public ResponseEntity<TokenPair> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {

        if (refreshToken == null || !jwtProvider.validate(refreshToken)) {
            return ResponseEntity.status(401).build();
        }


        String userId = jwtProvider.getSubject(refreshToken);


        User user = authService.getById(Long.parseLong(userId));


        TokenPair tokens = authService.issueTokens(user);


        ResponseCookie accessCookie = ResponseCookie.from("accessToken", tokens.accessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(tokens);
    }
}