
package com.thxgraduate.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Auth", description = "OAuth 인증 관련 API")
public class AuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final AuthService authService;

    @GetMapping("/kakao/callback")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 후 AccessToken과 RefreshToken 발급")
    public ResponseEntity<Void> kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        String kakaoToken = kakaoOAuthService.getAccessToken(code);
        KakaoUserInfoDto kakaoUser = kakaoOAuthService.getUserInfo(kakaoToken);

        User user = authService.loginOrRegister(kakaoUser);
        TokenPair tokens = authService.issueTokens(user);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", tokens.accessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .build();

        ResponseCookie linkCookie = ResponseCookie.from("userLink", user.getLink().toString())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, linkCookie.toString());

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, "http://localhost:5174/" + user.getLink())
                .build();
    }
}
