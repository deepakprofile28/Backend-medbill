package com.medbill.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.medbill.dto.LoginRequest;
import com.medbill.dto.LoginResponse;
import com.medbill.dto.RegisterRequest;
import com.medbill.dto.VerifyOtpRequest;
import com.medbill.entity.Company;
import com.medbill.entity.User;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.UserRepository;
import com.medbill.security.JwtService;
import com.medbill.service.AuthService;
import com.medbill.util.PhoneNumberValidator;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================================================
    // REGISTER
    // =========================================================

    @Override
    public String register(RegisterRequest request) {

        System.out.println("=================================");
        System.out.println("REGISTER API CALLED");

        // -----------------------------------------------------
        // Validate request
        // -----------------------------------------------------

        if (request == null) {
            throw new RuntimeException("Registration request is required");
        }

        // -----------------------------------------------------
        // Validate name
        // -----------------------------------------------------

        if (request.getName() == null ||
                request.getName().trim().isEmpty()) {

            throw new RuntimeException("Name is required");
        }

        // -----------------------------------------------------
        // Validate email
        // -----------------------------------------------------

        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty()) {

            throw new RuntimeException("Email is required");
        }

        // -----------------------------------------------------
        // Validate password
        // -----------------------------------------------------

        if (request.getPassword() == null ||
                request.getPassword().trim().isEmpty()) {

            throw new RuntimeException("Password is required");
        }

        // -----------------------------------------------------
        // Validate company ID
        // -----------------------------------------------------

        if (request.getCompanyId() == null) {

            throw new RuntimeException("Company ID is required");
        }

        // -----------------------------------------------------
        // Validate country code
        // -----------------------------------------------------

        if (request.getCountryCode() == null ||
                request.getCountryCode().trim().isEmpty()) {

            throw new RuntimeException("Country code is required");
        }

        // -----------------------------------------------------
        // Validate mobile
        // -----------------------------------------------------

        if (request.getMobile() == null ||
                request.getMobile().trim().isEmpty()) {

            throw new RuntimeException("Mobile number is required");
        }

        // -----------------------------------------------------
        // Normalize values
        // -----------------------------------------------------

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        String countryCode = request.getCountryCode()
                .trim();

        String mobile = request.getMobile()
                .replaceAll("\\D", "");

        System.out.println("Name        : " + request.getName());
        System.out.println("Email       : " + email);
        System.out.println("CountryCode : " + countryCode);
        System.out.println("Mobile      : " + mobile);

        // =====================================================
        // COUNTRY BASED PHONE VALIDATION
        // =====================================================

        if (!PhoneNumberValidator.isValid(countryCode, mobile)) {

            int requiredDigits =
                    PhoneNumberValidator.getRequiredDigits(countryCode);

            if (requiredDigits == 0) {

                throw new RuntimeException(
                        "Unsupported country code: " + countryCode
                );
            }

            throw new RuntimeException(
                    "Invalid mobile number for "
                            + countryCode
                            + ". Expected "
                            + requiredDigits
                            + " digits."
            );
        }

        System.out.println("Phone validation successful");

        // =====================================================
        // CHECK DUPLICATE EMAIL
        // =====================================================

        if (userRepository.existsByEmail(email)) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        // =====================================================
        // FIND COMPANY
        // =====================================================

        Company company = companyRepository
                .findById(request.getCompanyId())
                .orElse(null);

        if (company == null) {

            throw new RuntimeException(
                    "Company not found with ID: "
                            + request.getCompanyId()
            );
        }

        System.out.println(
                "Company found: " + company.getName()
        );

        // =====================================================
        // CHECK COMPANY STATUS
        // =====================================================

        if (company.getStatus() != null &&
                !"ACTIVE".equalsIgnoreCase(company.getStatus())) {

            throw new RuntimeException(
                    "Company is not active"
            );
        }

        // =====================================================
        // SAVE PHONE DETAILS
        // =====================================================

        company.setCountryCode(countryCode);
        company.setMobile(mobile);

        // =====================================================
        // SAVE BUSINESS EMAIL
        // =====================================================

        if (company.getEmail() == null ||
                company.getEmail().trim().isEmpty()) {

            company.setEmail(email);
        }

        // =====================================================
        // SAVE PASSWORD
        // =====================================================

        /*
         * Password must be BCrypt encoded only once.
         */

        company.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // =====================================================
        // GENERATE OTP
        // =====================================================

        String otp = String.format(
                "%06d",
                new Random().nextInt(1000000)
        );

        System.out.println(
                "Generated OTP: " + otp
        );

        // =====================================================
        // SAVE OTP
        // =====================================================

        company.setOtp(otp);

        company.setOtpExpiry(
                LocalDateTime.now().plusMinutes(5)
        );

        company.setOtpVerified(false);

        // =====================================================
        // SAVE COMPANY
        // =====================================================

        companyRepository.save(company);

        System.out.println(
                "Company saved successfully"
        );

        System.out.println(
                "Company ID   : " + company.getId()
        );

        System.out.println(
                "Country Code : " + company.getCountryCode()
        );

        System.out.println(
                "Mobile       : " + company.getMobile()
        );

        System.out.println(
                "OTP          : " + otp
        );

        System.out.println("=================================");

        // =====================================================
        // DEMO RESPONSE
        // =====================================================

        return "OTP sent successfully. DEMO OTP: " + otp;
    }


    // =========================================================
    // VERIFY OTP
    // =========================================================

    @Override
    public String verifyOtp(VerifyOtpRequest request) {

        System.out.println("=================================");
        System.out.println("VERIFY OTP API CALLED");

        // -----------------------------------------------------
        // Validate request
        // -----------------------------------------------------

        if (request == null) {

            throw new RuntimeException(
                    "OTP verification request is required"
            );
        }

        // -----------------------------------------------------
        // Country Code
        // -----------------------------------------------------

        if (request.getCountryCode() == null ||
                request.getCountryCode().trim().isEmpty()) {

            throw new RuntimeException(
                    "Country code is required"
            );
        }

        // -----------------------------------------------------
        // Mobile
        // -----------------------------------------------------

        if (request.getMobile() == null ||
                request.getMobile().trim().isEmpty()) {

            throw new RuntimeException(
                    "Mobile number is required"
            );
        }

        // -----------------------------------------------------
        // OTP
        // -----------------------------------------------------

        if (request.getOtp() == null ||
                request.getOtp().trim().isEmpty()) {

            throw new RuntimeException(
                    "OTP is required"
            );
        }

        // =====================================================
        // NORMALIZE
        // =====================================================

        String countryCode =
                request.getCountryCode().trim();

        String mobile =
                request.getMobile()
                        .replaceAll("\\D", "");

        String enteredOtp =
                request.getOtp().trim();

        System.out.println(
                "Country Code : " + countryCode
        );

        System.out.println(
                "Mobile       : " + mobile
        );

        System.out.println(
                "Entered OTP  : " + enteredOtp
        );

        // =====================================================
        // OTP FORMAT VALIDATION
        // =====================================================

        if (!enteredOtp.matches("\\d{6}")) {

            throw new RuntimeException(
                    "OTP must contain exactly 6 digits"
            );
        }

        // =====================================================
        // COUNTRY BASED PHONE VALIDATION
        // =====================================================

        if (!PhoneNumberValidator.isValid(
                countryCode,
                mobile)) {

            int requiredDigits =
                    PhoneNumberValidator.getRequiredDigits(
                            countryCode
                    );

            if (requiredDigits == 0) {

                throw new RuntimeException(
                        "Unsupported country code: "
                                + countryCode
                );
            }

            throw new RuntimeException(
                    "Invalid mobile number for "
                            + countryCode
                            + ". Expected "
                            + requiredDigits
                            + " digits."
            );
        }

        System.out.println(
                "Phone validation successful"
        );

        // =====================================================
        // FIND COMPANY
        // =====================================================

        Company company =
                companyRepository
                        .findByCountryCodeAndMobile(
                                countryCode,
                                mobile
                        )
                        .orElse(null);

        if (company == null) {

            throw new RuntimeException(
                    "Company not found for "
                            + countryCode
                            + " "
                            + mobile
            );
        }

        System.out.println(
                "Company found: "
                        + company.getName()
        );

        System.out.println(
                "Company ID: "
                        + company.getId()
        );

        // =====================================================
        // CHECK OTP
        // =====================================================

        if (company.getOtp() == null) {

            throw new RuntimeException(
                    "OTP not generated"
            );
        }

        // =====================================================
        // CHECK OTP EXPIRY
        // =====================================================

        if (company.getOtpExpiry() == null) {

            throw new RuntimeException(
                    "OTP expiry information missing"
            );
        }

        if (LocalDateTime.now()
                .isAfter(company.getOtpExpiry())) {

            throw new RuntimeException(
                    "OTP expired. Please request a new OTP."
            );
        }

        // =====================================================
        // COMPARE OTP
        // =====================================================

        if (!company.getOtp().equals(enteredOtp)) {

            System.out.println(
                    "OTP mismatch"
            );

            throw new RuntimeException(
                    "Invalid OTP"
            );
        }

        System.out.println(
                "OTP matched successfully"
        );

        // =====================================================
        // MARK OTP VERIFIED
        // =====================================================

        company.setOtpVerified(true);

        company.setOtp(null);

        company.setOtpExpiry(null);

        // =====================================================
        // SAVE COMPANY
        // =====================================================

        companyRepository.save(company);

        System.out.println(
                "OTP verification successful"
        );

        // =====================================================
        // COMPANY EMAIL
        // =====================================================

        if (company.getEmail() == null ||
                company.getEmail().trim().isEmpty()) {

            throw new RuntimeException(
                    "Company email is missing"
            );
        }

        String email =
                company.getEmail()
                        .trim()
                        .toLowerCase();

        // =====================================================
        // CHECK EXISTING USER
        // =====================================================

        Optional<User> existingUser =
                userRepository.findByEmail(email);

        if (existingUser.isPresent()) {

            throw new RuntimeException(
                    "User already exists with email: "
                            + email
            );
        }

        // =====================================================
        // CREATE USER
        // =====================================================

        User user = new User();

        // -----------------------------------------------------
        // User Name
        // -----------------------------------------------------

        user.setName(
                company.getName()
        );

        // -----------------------------------------------------
        // Email
        // -----------------------------------------------------

        user.setEmail(email);

        // -----------------------------------------------------
        // Password
        // -----------------------------------------------------

        /*
         * Company password is already BCrypt encoded.
         *
         * DO NOT encode again.
         */

        if (company.getPassword() == null ||
                company.getPassword().trim().isEmpty()) {

            throw new RuntimeException(
                    "Company password is missing"
            );
        }

        user.setPassword(
                company.getPassword()
        );

        // -----------------------------------------------------
        // Role
        // -----------------------------------------------------

        user.setRole("ADMIN");

        // -----------------------------------------------------
        // Active
        // -----------------------------------------------------

        user.setActive(true);

        // -----------------------------------------------------
        // Company
        // -----------------------------------------------------

        user.setCompany(company);

        // =====================================================
        // SAVE USER
        // =====================================================

        User savedUser =
                userRepository.save(user);

        // =====================================================
        // LOG
        // =====================================================

        System.out.println("=================================");
        System.out.println("USER SAVED SUCCESSFULLY");

        System.out.println(
                "User ID      : "
                        + savedUser.getId()
        );

        System.out.println(
                "User Name    : "
                        + savedUser.getName()
        );

        System.out.println(
                "User Email   : "
                        + savedUser.getEmail()
        );

        System.out.println(
                "Company ID   : "
                        + company.getId()
        );

        System.out.println(
                "Country Code : "
                        + company.getCountryCode()
        );

        System.out.println(
                "Mobile       : "
                        + company.getMobile()
        );

        System.out.println("=================================");

        return "OTP verified successfully. "
                + "User registered successfully.";
    }


    // =========================================================
    // LOGIN
    // =========================================================

    @Override
    public LoginResponse login(LoginRequest request) {

        System.out.println("=================================");
        System.out.println("LOGIN API CALLED");

        // -----------------------------------------------------
        // Validate request
        // -----------------------------------------------------

        if (request == null) {

            throw new RuntimeException(
                    "Login request is required"
            );
        }

        // -----------------------------------------------------
        // Validate email
        // -----------------------------------------------------

        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty()) {

            throw new RuntimeException(
                    "Email is required"
            );
        }

        // -----------------------------------------------------
        // Validate password
        // -----------------------------------------------------

        if (request.getPassword() == null ||
                request.getPassword().trim().isEmpty()) {

            throw new RuntimeException(
                    "Password is required"
            );
        }

        // =====================================================
        // NORMALIZE EMAIL
        // =====================================================

        String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();

        System.out.println(
                "Login Email: " + email
        );

        // =====================================================
        // FIND USER
        // =====================================================

        Optional<User> userOptional =
                userRepository.findByEmail(email);

        System.out.println(
                "User found: "
                        + userOptional.isPresent()
        );

        if (userOptional.isEmpty()) {

            throw new RuntimeException(
                    "Invalid Email or Password"
            );
        }

        User user =
                userOptional.get();

        System.out.println(
                "User: "
                        + user.getName()
        );

        // =====================================================
        // CHECK USER ACTIVE
        // =====================================================

        if (!Boolean.TRUE.equals(
                user.getActive())) {

            throw new RuntimeException(
                    "User account is inactive"
            );
        }

        // =====================================================
        // CHECK COMPANY
        // =====================================================

        if (user.getCompany() == null) {

            throw new RuntimeException(
                    "User is not assigned to any company"
            );
        }

        Company company =
                user.getCompany();

        // =====================================================
        // CHECK COMPANY ID
        // =====================================================

        if (company.getId() == null) {

            throw new RuntimeException(
                    "Company ID is missing"
            );
        }

        // =====================================================
        // CHECK COMPANY STATUS
        // =====================================================

        if (company.getStatus() != null &&
                !"ACTIVE".equalsIgnoreCase(
                        company.getStatus())) {

            throw new RuntimeException(
                    "Company is not active"
            );
        }

        // =====================================================
        // CHECK PASSWORD
        // =====================================================

        if (user.getPassword() == null ||
                user.getPassword().trim().isEmpty()) {

            throw new RuntimeException(
                    "User password is missing"
            );
        }

        // =====================================================
        // PASSWORD MATCH
        // =====================================================

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            System.out.println(
                    "Password mismatch!"
            );

            throw new RuntimeException(
                    "Invalid Email or Password"
            );
        }

        System.out.println(
                "Password matched successfully"
        );

        // =====================================================
        // GENERATE JWT
        // =====================================================

        String token =
                jwtService.generateToken(user);

        System.out.println(
                "JWT generated successfully"
        );

        // =====================================================
        // LOGIN SUCCESS
        // =====================================================

        System.out.println(
                "Login successful!"
        );

        System.out.println(
                "User         : "
                        + user.getName()
        );

        System.out.println(
                "User ID      : "
                        + user.getId()
        );

        System.out.println(
                "Company ID   : "
                        + company.getId()
        );

        System.out.println(
                "Company      : "
                        + company.getName()
        );

        System.out.println(
                "Country Code : "
                        + company.getCountryCode()
        );

        System.out.println(
                "Mobile       : "
                        + company.getMobile()
        );

        System.out.println("=================================");

        // =====================================================
        // LOGIN RESPONSE
        // =====================================================

        return LoginResponse.builder()

                .userName(
                        user.getName()
                )

                .role(
                        user.getRole()
                )

                .token(
                        token
                )

                .companyId(
                        company.getId()
                )

                .companyName(
                        company.getName()
                )

                .build();
    }
}