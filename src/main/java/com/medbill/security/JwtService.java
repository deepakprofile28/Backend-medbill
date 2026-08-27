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

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // ================= SECRET KEY =================

    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ================= GENERATE TOKEN =================

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    // ================= EXTRACT EMAIL =================

    public String extractEmail(String token) {

        return extractAllClaims(token).getSubject();
    }

    // ================= VALIDATE TOKEN =================

    public boolean isTokenValid(String token, User user) {

        try {

            String email = extractEmail(token);

            return email.equals(user.getEmail())
                    && !isTokenExpired(token);

        } catch (Exception e) {

            return false;
        }
    }

    // ================= CHECK EXPIRATION =================

    private boolean isTokenExpired(String token) {

        Date expirationDate =
                extractAllClaims(token).getExpiration();

        return expirationDate.before(new Date());
    }

    // ================= EXTRACT CLAIMS =================

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}