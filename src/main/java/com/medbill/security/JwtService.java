package com.medbill.security;

import org.springframework.stereotype.Service;

import com.medbill.entity.User;

@Service
public class JwtService {

    public String generateToken(User user) {

        // JWT token generation logic
        return "token";
    }
}