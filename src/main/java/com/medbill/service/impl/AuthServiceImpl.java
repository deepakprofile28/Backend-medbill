package com.medbill.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.medbill.dto.LoginRequest;
import com.medbill.dto.LoginResponse;
import com.medbill.entity.User;
import com.medbill.repository.UserRepository;
import com.medbill.security.JwtService;
import com.medbill.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

    	System.out.println("1. Email: " + request.getEmail());

    	Optional<User> userOptional =
    	        userRepository.findByEmail(request.getEmail());

    	System.out.println("2. User found: " + userOptional.isPresent());

    	if (userOptional.isEmpty()) {
    	    throw new RuntimeException("Invalid Email");
    	}

    	User user = userOptional.get();

    	System.out.println("3. User: " + user.getName());
    	System.out.println("4. Password exists: " + (user.getPassword() != null));

    	if (!passwordEncoder.matches(
    	        request.getPassword(),
    	        user.getPassword())) {

    	    throw new RuntimeException("Invalid Email or Password");
    	}

    	System.out.println("5. Password matched");

    	String token = jwtService.generateToken(user);

    	System.out.println("6. JWT generated");

    	return LoginResponse.builder()
    	        .userName(user.getName())
    	        .role(user.getRole())
    	        .token(token)
    	        .build();}
}