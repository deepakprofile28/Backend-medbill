package com.medbill.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.medbill.dto.LoginRequest;
import com.medbill.dto.LoginResponse;
import com.medbill.entity.User;
import com.medbill.repository.UserRepository;
import com.medbill.service.AuthService;
import com.medbill.security.JwtService;
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Optional<User> userOptional =
                userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid Email");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid Email or Password");
        }

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new RuntimeException("User Disabled");
        }

        return LoginResponse.builder()
                .userName(user.getName())
                .role(user.getRole())
                .token(jwtService.generateToken(user))
                .build();
}