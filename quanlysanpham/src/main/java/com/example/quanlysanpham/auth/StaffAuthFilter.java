package com.example.quanlysanpham.auth;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class StaffAuthFilter extends OncePerRequestFilter {

    private final TokenStore tokenStore;
    private final UserRepository userRepository;

    public StaffAuthFilter(TokenStore tokenStore, UserRepository userRepository) {
        this.tokenStore = tokenStore;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // chỉ filter các endpoint staff
        String path = request.getRequestURI();
        return path == null || !path.startsWith("/api/staff/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractBearerToken(request.getHeader("Authorization"));
        if (token == null || !tokenStore.isValid(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Missing/invalid token");
            return;
        }

        Long userId = tokenStore.getUserId(token);
        User u = userRepository.findById(userId).orElse(null);
        if (u == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("User not found for token");
            return;
        }

        // Chỉ cho STAFF gọi /api/staff/**
        if (u.getRole() == null || !u.getRole().equalsIgnoreCase("STAFF")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Forbidden: not STAFF");
            return;
        }

        // controller lấy userId từ request:
        request.setAttribute("authUserId", userId);

        filterChain.doFilter(request, response);
    }

    private String extractBearerToken(String authorization) {
        if (authorization == null) return null;
        String prefix = "Bearer ";
        if (!authorization.startsWith(prefix)) return null;
        return authorization.substring(prefix.length()).trim();
    }
}
