package com.medbill.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.medbill.entity.User;
import com.medbill.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // ==========================================
        // GET AUTHORIZATION HEADER
        // ==========================================

        final String authHeader =
                request.getHeader("Authorization");

        // No Authorization header
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ==========================================
        // EXTRACT JWT TOKEN
        // ==========================================

        final String token =
                authHeader.substring(7);

        try {

            // ==========================================
            // EXTRACT EMAIL FROM TOKEN
            // ==========================================

            String email =
                    jwtService.extractEmail(token);

            // ==========================================
            // CHECK USER NOT ALREADY AUTHENTICATED
            // ==========================================

            if (email != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // ==========================================
                // FIND USER FROM DATABASE
                // ==========================================

                User user =
                        userRepository
                                .findByEmail(email)
                                .orElse(null);

                // ==========================================
                // VALIDATE TOKEN
                // ==========================================

                if (user != null &&
                        jwtService.isTokenValid(token, user)) {

                    // ==========================================
                    // CREATE AUTHENTICATION
                    // ==========================================

                    UserDetails userDetails =
                            org.springframework.security.core.userdetails.User
                                    .withUsername(user.getEmail())
                                    .password(user.getPassword())
                                    .authorities(
                                            user.getRole()
                                    )
                                    .build();

                    UsernamePasswordAuthenticationToken
                            authentication =
                                new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                                );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // ==========================================
                    // SET SECURITY CONTEXT
                    // ==========================================

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT Authentication failed: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // CONTINUE REQUEST
        // ==========================================

        filterChain.doFilter(request, response);
    }
}