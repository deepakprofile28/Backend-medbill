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
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

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

        // =====================================================
        // GET AUTHORIZATION HEADER
        // =====================================================

        String authHeader =
                request.getHeader("Authorization");

        // No token
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // =====================================================
        // GET TOKEN
        // =====================================================

        String token =
                authHeader.substring(7).trim();

        if (token.isEmpty()) {

            filterChain.doFilter(request, response);
            return;
        }

        try {

            // =================================================
            // EXTRACT EMAIL
            // =================================================

            String email =
                    jwtService.extractEmail(token);

            // =================================================
            // EXTRACT COMPANY ID
            // =================================================

            Long companyId =
                    jwtService.extractCompanyId(token);

            System.out.println(
                    "JWT Email      : " + email
            );

            System.out.println(
                    "JWT Company ID : " + companyId
            );

            // =================================================
            // CHECK AUTHENTICATION
            // =================================================

            if (email != null &&
                    companyId != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // =================================================
                // FIND USER
                // =================================================

                User user =
                        userRepository
                                .findByEmail(email)
                                .orElse(null);

                // =================================================
                // VALIDATE USER
                // =================================================

                if (user != null &&
                        user.getCompany() != null &&
                        user.getCompany().getId() != null &&
                        user.getCompany()
                                .getId()
                                .equals(companyId) &&
                        Boolean.TRUE.equals(
                                user.getActive()
                        ) &&
                        jwtService.isTokenValid(
                                token,
                                user
                        )) {

                    System.out.println(
                            "User Company ID: "
                                    + user.getCompany().getId()
                    );

                    System.out.println(
                            "Tenant validation successful"
                    );

                    // =================================================
                    // USER DETAILS
                    // =================================================

                    UserDetails userDetails =
                            org.springframework.security.core.userdetails.User
                                    .withUsername(
                                            user.getEmail()
                                    )
                                    .password(
                                            user.getPassword()
                                    )
                                    .authorities(
                                            user.getRole()
                                    )
                                    .build();

                    // =================================================
                    // AUTHENTICATION
                    // =================================================

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // =================================================
                    // SET SECURITY CONTEXT
                    // =================================================

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );

                } else {

                    System.out.println(
                            "JWT validation failed: "
                                    + "User / Company / Token mismatch"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT Authentication failed: "
                            + e.getMessage()
            );

            // Don't stop request here.
            // Spring Security will decide whether
            // authentication is required.
        }

        // =====================================================
        // CONTINUE REQUEST
        // =====================================================

        filterChain.doFilter(request, response);
    }
}