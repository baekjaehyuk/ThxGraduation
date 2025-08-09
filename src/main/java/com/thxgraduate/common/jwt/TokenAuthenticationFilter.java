package com.thxgraduate.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String COOKIE_ACCESS_TOKEN = "accessToken";

    private final JwtProvider jwtProvider;

    public TokenAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        try {
            // 이미 인증된 요청이면 재파싱 X
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String token = resolveToken(req);

                if (token != null && jwtProvider.validate(token)) {
                    String userId = jwtProvider.getSubject(token);
                    // 권한 필요하면 추가
                    var auth = new UsernamePasswordAuthenticationToken(userId, null, List.of());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception ignore) {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(req, res);
    }

    private String resolveToken(HttpServletRequest req) {
        String header = req.getHeader(HEADER_AUTHORIZATION);
        String bearer = resolveBearer(header);
        if (bearer != null) return bearer;
        return resolveCookie(req, COOKIE_ACCESS_TOKEN);
    }

    private String resolveBearer(String header) {
        if (header == null) return null;
        if (header.regionMatches(true, 0, TOKEN_PREFIX, 0, TOKEN_PREFIX.length())) {
            return header.substring(TOKEN_PREFIX.length()).trim();
        }
        return null;
    }

    private String resolveCookie(HttpServletRequest req, String name) {
        if (req.getCookies() == null) return null;
        for (var c : req.getCookies()) {
            if (name.equals(c.getName())) {
                String v = c.getValue();
                return (v == null || v.isBlank()) ? null : v.trim();
            }
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String uri = req.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) return true;
        return uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/api-docs")
                || uri.startsWith("/oauth")              // 카카오 로그인/콜백
                || uri.equals("/login-success")
                || uri.equals("/login-failure");
    }
}
