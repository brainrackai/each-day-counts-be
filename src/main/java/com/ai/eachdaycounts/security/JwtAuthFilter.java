package com.ai.eachdaycounts.security;

import com.ai.eachdaycounts.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        if (request.getRequestURI().contains("/auth/login") || 
            request.getRequestURI().contains("/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            token = Arrays.stream(cookies)
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if (token != null && jwtService.isTokenValid(token, jwtService.extractEmail(token))) {
            String role = jwtService.extractRole(token);
            
            // Check for admin-only endpoints
            if (request.getRequestURI().contains("/auth/users") && !"ADMIN".equals(role)) {
                response.setContentType("application/json");
                response.setStatus(403); // Forbidden
                response.getWriter().write("{\"status\": 403, \"error\": \"Forbidden: Admin access required\"}");
                return;
            }
            
            filterChain.doFilter(request, response);
        } else {
            response.setContentType("application/json");
            response.setStatus(401);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.getWriter().write("{\"status\": 401, \"error\": \"Unauthorized: Invalid or missing token\"}");
        }
    }
} 