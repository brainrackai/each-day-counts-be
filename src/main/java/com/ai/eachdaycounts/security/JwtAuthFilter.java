package com.ai.eachdaycounts.security;

import com.ai.eachdaycounts.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String token = extractTokenFromCookies(request);
            logger.debug("Extracted token: {}", token);

            if (token != null && jwtService.validateToken(token)) {
                String userEmail = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);
                logger.debug("Valid token for user: {} with role: {}", userEmail, role);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userEmail,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Authentication set in SecurityContext");
            }
        } catch (Exception e) {
            logger.error("JWT Authentication failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/login") || path.startsWith("/auth/register");
    }
} 