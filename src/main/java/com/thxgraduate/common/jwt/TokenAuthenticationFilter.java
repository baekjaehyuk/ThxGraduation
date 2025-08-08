package com.thxgraduate.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public TokenAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        try {
            String token = resolveBearer(req.getHeader("Authorization"));
            if (token != null && jwtProvider.validate(token)) {
                String userId = jwtProvider.getSubject(token); // sub에 userId 저장
                var auth = new UsernamePasswordAuthenticationToken(userId, null, List.of()); // 권한 비움
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception ignore) {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(req, res);
    }

    private String resolveBearer(String header) {
        if (header == null) return null;
        if (header.toLowerCase(Locale.ROOT).startsWith("bearer ")) {
            return header.substring(7).trim();
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
