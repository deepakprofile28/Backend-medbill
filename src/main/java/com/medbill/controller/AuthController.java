package com.medbill.controller;

import com.medbill.dto.RegisterRequest;
import com.medbill.entity.User;
import com.medbill.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        System.out.println("=================================");
        System.out.println("REGISTER API CALLED");
        System.out.println("Name  : " + request.getName());
        System.out.println("Email : " + request.getEmail());

        // Check duplicate email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            System.out.println("Email already exists!");

            return ResponseEntity
                    .badRequest()
                    .body("Email already registered");
        }

        // Create new User
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(request.getRole());
        user.setActive(request.isActive());

        // Save user into database
        userRepository.save(user);

        System.out.println("User saved successfully!");
        System.out.println("=================================");

        return ResponseEntity.ok("User registered successfully");
    }
}