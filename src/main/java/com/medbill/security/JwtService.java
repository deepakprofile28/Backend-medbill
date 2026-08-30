package com.medbill.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.medbill.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // =====================================================
    // JWT CONFIGURATION
    // =====================================================

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;


    // =====================================================
    // GET SIGNING KEY
    // =====================================================

    private Key getSigningKey() {

        if (secret == null || secret.trim().isEmpty()) {

            throw new RuntimeException(
                    "JWT secret is not configured"
            );
        }

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }


    // =====================================================
    // GENERATE JWT TOKEN
    // =====================================================

    public String generateToken(User user) {

        // -------------------------------------------------
        // USER CHECK
        // -------------------------------------------------

        if (user == null) {

            throw new RuntimeException(
                    "User cannot be null"
            );
        }

        // -------------------------------------------------
        // USER ID CHECK
        // -------------------------------------------------

        if (user.getId() == null) {

            throw new RuntimeException(
                    "User ID is missing"
            );
        }

        // -------------------------------------------------
        // EMAIL CHECK
        // -------------------------------------------------

        if (user.getEmail() == null ||
                user.getEmail().trim().isEmpty()) {

            throw new RuntimeException(
                    "User email is missing"
            );
        }

        // -------------------------------------------------
        // COMPANY CHECK
        // -------------------------------------------------

        if (user.getCompany() == null) {

            throw new RuntimeException(
                    "User is not assigned to any company"
            );
        }

        // -------------------------------------------------
        // COMPANY ID CHECK
        // -------------------------------------------------

        if (user.getCompany().getId() == null) {

            throw new RuntimeException(
                    "Company ID is missing"
            );
        }


        // =================================================
        // CREATE TOKEN
        // =================================================

        return Jwts.builder()

                // -----------------------------------------
                // SUBJECT = EMAIL
                // -----------------------------------------

                .setSubject(
                        user.getEmail()
                )

                // -----------------------------------------
                // USER ID
                // -----------------------------------------

                .claim(
                        "userId",
                        user.getId()
                )

                // -----------------------------------------
                // ROLE
                // -----------------------------------------

                .claim(
                        "role",
                        user.getRole()
                )

                // -----------------------------------------
                // COMPANY ID
                // -----------------------------------------

                .claim(
                        "companyId",
                        user.getCompany().getId()
                )

                // -----------------------------------------
                // ISSUED DATE
                // -----------------------------------------

                .setIssuedAt(
                        new Date()
                )

                // -----------------------------------------
                // EXPIRATION
                // -----------------------------------------

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )

                // -----------------------------------------
                // SIGN TOKEN
                // -----------------------------------------

                .signWith(
                        getSigningKey()
                )

                // -----------------------------------------
                // BUILD TOKEN
                // -----------------------------------------

                .compact();
    }


    // =====================================================
    // EXTRACT EMAIL
    // =====================================================

    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();
    }


    // =====================================================
    // EXTRACT USER ID
    // =====================================================

    public Long extractUserId(String token) {

        return extractAllClaims(token)
                .get("userId", Long.class);
    }


    // =====================================================
    // EXTRACT ROLE
    // =====================================================

    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }


    // =====================================================
    // EXTRACT COMPANY ID
    // =====================================================

    public Long extractCompanyId(String token) {

        return extractAllClaims(token)
                .get("companyId", Long.class);
    }


    // =====================================================
    // VALIDATE TOKEN
    // =====================================================

    public boolean isTokenValid(
            String token,
            User user) {

        try {

            // ---------------------------------------------
            // TOKEN EMAIL
            // ---------------------------------------------

            String tokenEmail =
                    extractEmail(token);


            // ---------------------------------------------
            // TOKEN COMPANY ID
            // ---------------------------------------------

            Long tokenCompanyId =
                    extractCompanyId(token);


            // ---------------------------------------------
            // USER COMPANY ID
            // ---------------------------------------------

            Long userCompanyId =

                    user.getCompany() != null
                            ? user.getCompany().getId()
                            : null;


            // ---------------------------------------------
            // VALIDATION
            // ---------------------------------------------

            return tokenEmail != null

                    && user.getEmail() != null

                    && tokenEmail.equals(
                            user.getEmail()
                    )

                    && tokenCompanyId != null

                    && userCompanyId != null

                    && tokenCompanyId.equals(
                            userCompanyId
                    )

                    && !isTokenExpired(token);

        } catch (Exception e) {

            System.out.println(
                    "JWT validation failed: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // =====================================================
    // CHECK TOKEN EXPIRATION
    // =====================================================

    private boolean isTokenExpired(String token) {

        Date expirationDate =
                extractAllClaims(token)
                        .getExpiration();

        return expirationDate.before(
                new Date()
        );
    }


    // =====================================================
    // EXTRACT ALL CLAIMS
    // =====================================================

    private Claims extractAllClaims(String token) {

        return Jwts.parser()

                // -----------------------------------------
                // SECRET KEY
                // -----------------------------------------

                .setSigningKey(
                        getSigningKey()
                )

                // -----------------------------------------
                // PARSE JWT
                // -----------------------------------------

                .build()

                .parseClaimsJws(token)

                // -----------------------------------------
                // GET BODY / CLAIMS
                // -----------------------------------------

                .getBody();
    }
}