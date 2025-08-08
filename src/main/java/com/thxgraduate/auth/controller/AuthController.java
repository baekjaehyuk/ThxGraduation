package com.thxgraduate.auth.controller;

import com.thxgraduate.auth.controller.dto.AccessTokenResponse;
import com.thxgraduate.auth.controller.dto.KakaoUserInfoDto;
import com.thxgraduate.auth.controller.dto.TokenPair;
import com.thxgraduate.auth.service.AuthService;
import com.thxgraduate.auth.service.KakaoOAuthService;
import com.thxgraduate.auth.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class AuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final AuthService authService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<AccessTokenResponse> kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        String kakaoToken = kakaoOAuthService.getAccessToken(code);
        KakaoUserInfoDto kakaoUser = kakaoOAuthService.getUserInfo(kakaoToken);

        User user = authService.loginOrRegister(kakaoUser);
        TokenPair tokens = authService.issueTokens(user);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new AccessTokenResponse(tokens.accessToken()));
    }
}
