package com.medbill.controller;

import com.medbill.dto.LoginRequest;
import com.medbill.dto.LoginResponse;
import com.medbill.dto.RegisterRequest;
import com.medbill.entity.Company;
import com.medbill.entity.User;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.UserRepository;
import com.medbill.security.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =====================================================
    // REGISTER
    // =====================================================

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        System.out.println("=================================");
        System.out.println("REGISTER API CALLED");

        System.out.println("Name      : " + request.getName());
        System.out.println("Email     : " + request.getEmail());
        System.out.println("Company ID: " + request.getCompanyId());

        // =================================================
        // CHECK DUPLICATE EMAIL
        // =================================================

        if (userRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            System.out.println("Email already exists!");

            return ResponseEntity
                    .badRequest()
                    .body("Email already registered");
        }

        // =================================================
        // CHECK COMPANY ID
        // =================================================

        if (request.getCompanyId() == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Company ID is required");
        }

        // =================================================
        // FIND COMPANY
        // =================================================

        Company company = companyRepository
                .findById(request.getCompanyId())
                .orElse(null);

        if (company == null) {

            System.out.println("Company not found!");

            return ResponseEntity
                    .badRequest()
                    .body("Company not found with ID: "
                            + request.getCompanyId());
        }

        // =================================================
        // CREATE USER
        // =================================================

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(request.getRole());

        user.setActive(request.isActive());

        // =================================================
        // ASSIGN COMPANY
        // =================================================

        user.setCompany(company);

        // =================================================
        // SAVE USER
        // =================================================

        userRepository.save(user);

        System.out.println("User saved successfully!");

        System.out.println("User ID   : " + user.getId());
        System.out.println("Company ID: " + company.getId());
        System.out.println("Company   : " + company.getName());

        System.out.println("=================================");

        return ResponseEntity.ok(
                "User registered successfully"
        );
    }

    // =====================================================
    // LOGIN
    // =====================================================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        System.out.println("=================================");
        System.out.println("LOGIN API CALLED");

        System.out.println("Email : " + request.getEmail());

        // =================================================
        // FIND USER
        // =================================================

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {

            System.out.println("User not found!");

            return ResponseEntity
                    .badRequest()
                    .body("Invalid Email or Password");
        }

        // =================================================
        // CHECK PASSWORD
        // =================================================

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            System.out.println("Password mismatch!");

            return ResponseEntity
                    .badRequest()
                    .body("Invalid Email or Password");
        }

        // =================================================
        // GENERATE JWT
        // =================================================

        String token = jwtService.generateToken(user);

        System.out.println("Login successful!");

        System.out.println("User       : "
                + user.getName());

        System.out.println("Company ID : "
                + (user.getCompany() != null
                ? user.getCompany().getId()
                : null));

        System.out.println("Company Name : "
                + (user.getCompany() != null
                ? user.getCompany().getName()
                : null));

        // =================================================
        // LOGIN RESPONSE
        // =================================================

        LoginResponse response = LoginResponse.builder()

                .userName(user.getName())

                .role(user.getRole())

                .token(token)

                .companyId(
                        user.getCompany() != null
                                ? user.getCompany().getId()
                                : null
                )

                .companyName(
                        user.getCompany() != null
                                ? user.getCompany().getName()
                                : null
                )

                .build();

        System.out.println("=================================");

        return ResponseEntity.ok(response);
    }
}