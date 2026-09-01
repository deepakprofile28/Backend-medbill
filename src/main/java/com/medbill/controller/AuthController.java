package com.medbill.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medbill.dto.CompanyRegistrationRequest;
import com.medbill.dto.LoginRequest;
import com.medbill.dto.LoginResponse;
import com.medbill.dto.RegisterRequest;
import com.medbill.dto.VerifyOtpRequest;
import com.medbill.entity.Company;
import com.medbill.entity.User;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.UserRepository;
import com.medbill.security.JwtService;
import com.medbill.service.EmailService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthController(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    // =====================================================
    // 1. REGISTER COMPANY (ONBOARDING + OTP GENERATION)
    // POST /api/auth/register-company
    // =====================================================
    @PostMapping("/register-company")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyRegistrationRequest request) {
        System.out.println("=================================");
        System.out.println("REGISTER COMPANY API CALLED");

        try {
            String companyName = request.getEffectiveCompanyName();
            String email = request.getEffectiveEmail();
            String countryCode = request.getEffectiveCountryCode();
            String mobile = request.getEffectiveMobile();
            String rawPassword = request.getPassword();

            if (companyName.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Pharmacy / Company name is required"));
            }

            if (email.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }

            if (rawPassword == null || rawPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
            }

            // Check if Company Email already exists
            Optional<Company> existingCompanyByEmail = companyRepository.findByEmail(email);
            Company companyToSave;

            if (existingCompanyByEmail.isPresent()) {
                companyToSave = existingCompanyByEmail.get();
                companyToSave.setName(companyName);
                if (!mobile.isEmpty()) {
                    companyToSave.setMobile(mobile);
                }
                companyToSave.setCountryCode(countryCode);
                if (request.getAddress() != null) {
                    companyToSave.setAddress(request.getAddress());
                }
                companyToSave.setPassword(passwordEncoder.encode(rawPassword));
            } else {
                companyToSave = new Company();
                companyToSave.setName(companyName);
                companyToSave.setEmail(email);
                companyToSave.setCountryCode(countryCode);
                companyToSave.setMobile(mobile);
                companyToSave.setAddress(request.getAddress() != null ? request.getAddress() : "Chennai");
                companyToSave.setStatus("ACTIVE");
                companyToSave.setPlan("BASIC");
                companyToSave.setCreatedAt(LocalDateTime.now());
                companyToSave.setPassword(passwordEncoder.encode(rawPassword));
            }

            // Generate 6-digit OTP
            String otp = String.format("%06d", new Random().nextInt(1000000));
            companyToSave.setOtp(otp);
            companyToSave.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            companyToSave.setOtpVerified(false);

            // SAVE TO MYSQL companies TABLE
            companyToSave.setStatus("PENDING_VERIFICATION");
            Company savedCompany = companyRepository.save(companyToSave);
            System.out.println("Company saved in MySQL companies table with ID: " + savedCompany.getId());

            // CREATE OR UPDATE ADMIN USER IN users TABLE (INACTIVE UNTIL OTP IS VERIFIED)
            Optional<User> existingUser = userRepository.findByEmail(email);
            User adminUser;
            if (existingUser.isPresent()) {
                adminUser = existingUser.get();
                adminUser.setName(request.getEffectiveOwnerName());
                adminUser.setPassword(passwordEncoder.encode(rawPassword));
                adminUser.setCompany(savedCompany);
                adminUser.setActive(false);
            } else {
                adminUser = new User();
                adminUser.setName(request.getEffectiveOwnerName());
                adminUser.setEmail(email);
                adminUser.setPassword(passwordEncoder.encode(rawPassword));
                adminUser.setRole("ADMIN");
                adminUser.setActive(false);
                adminUser.setCompany(savedCompany);
            }
            userRepository.save(adminUser);
            System.out.println("Admin user saved in MySQL users table (Pending OTP verification): " + adminUser.getEmail());

            // Send OTP Email via Gmail SMTP Asynchronously
            emailService.sendOtpEmail(email, request.getEffectiveOwnerName(), otp);

            // Build Response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pharmacy account registered successfully! OTP sent to your email & mobile.");
            response.put("id", savedCompany.getId());
            response.put("companyId", savedCompany.getId());
            response.put("company", savedCompany);
            response.put("otp", otp);
            response.put("otpResponse", Map.of(
                    "otp", otp,
                    "message", "OTP sent successfully"
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Registration failed: " + e.getMessage()));
        }
    }

    // =====================================================
    // REGISTER STAFF / USER (LINK TO STORE COMPANY)
    // POST /api/auth/register & POST /api/auth/register-staff
    // =====================================================
    @PostMapping({"/register", "/register-staff"})
    public ResponseEntity<?> registerStaff(@RequestBody RegisterRequest request) {
        System.out.println("=================================");
        System.out.println("REGISTER STAFF / USER API CALLED FOR: " + request.getEmail());

        try {
            String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
            String name = request.getName() != null ? request.getName().trim() : "Staff Member";
            String rawPassword = request.getPassword();
            String role = request.getRole() != null && !request.getRole().trim().isEmpty() ? request.getRole().trim().toUpperCase() : "PHARMACIST";

            if (email.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }

            if (rawPassword == null || rawPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
            }

            // Find Company: 1) by companyId, 2) by companyName, 3) by current authenticated user
            Company company = null;
            if (request.getCompanyId() != null) {
                company = companyRepository.findById(request.getCompanyId()).orElse(null);
            }
            if (company == null && request.getCompanyName() != null && !request.getCompanyName().trim().isEmpty()) {
                String reqCompName = request.getCompanyName().trim();
                var allComps = companyRepository.findAll();
                for (Company c : allComps) {
                    if (c.getName() != null && (c.getName().equalsIgnoreCase(reqCompName)
                            || c.getName().toLowerCase().contains(reqCompName.toLowerCase())
                            || reqCompName.toLowerCase().contains(c.getName().toLowerCase()))) {
                        company = c;
                        break;
                    }
                }
            }
            if (company == null) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                    Optional<User> loggedInOpt = userRepository.findByEmail(auth.getName());
                    if (loggedInOpt.isPresent() && loggedInOpt.get().getCompany() != null) {
                        company = loggedInOpt.get().getCompany();
                    }
                }
            }
            if (company == null) {
                var allComps = companyRepository.findAll();
                if (!allComps.isEmpty()) {
                    company = allComps.get(0);
                }
            }

            // Check if User already exists
            Optional<User> existingUserOpt = userRepository.findByEmail(email);
            User user;
            if (existingUserOpt.isPresent()) {
                user = existingUserOpt.get();
                user.setName(name);
                user.setPassword(passwordEncoder.encode(rawPassword));
                user.setRole(role);
                user.setActive(true);
                if (company != null) {
                    user.setCompany(company);
                }
            } else {
                user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(rawPassword));
                user.setRole(role);
                user.setActive(true);
                user.setCompany(company);
            }

            User savedUser = userRepository.save(user);
            System.out.println("Staff user registered in MySQL with ID: " + savedUser.getId() + ", Company ID: " + (company != null ? company.getId() : "null"));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Staff registered successfully!");
            response.put("id", savedUser.getId());
            response.put("name", savedUser.getName());
            response.put("email", savedUser.getEmail());
            response.put("role", savedUser.getRole());
            response.put("companyId", company != null ? company.getId() : null);
            response.put("companyName", company != null ? company.getName() : null);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Failed to register staff: " + e.getMessage()));
        }
    }

    // =====================================================
    // RESEND OTP
    // POST /api/auth/resend-otp & POST /api/auth/send-otp
    // =====================================================
    @PostMapping({"/resend-otp", "/send-otp"})
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> request) {
        System.out.println("=================================");
        System.out.println("RESEND OTP API CALLED");

        try {
            String countryCode = request.getOrDefault("countryCode", "+91").trim();
            String mobile = request.getOrDefault("mobile", "").replaceAll("\\D", "");
            String email = request.getOrDefault("email", "").trim().toLowerCase();

            System.out.println("Resending OTP for countryCode: " + countryCode + ", mobile: " + mobile + ", email: " + email);

            // Generate new 6-digit OTP
            String newOtp = String.format("%06d", new Random().nextInt(1000000));

            // Find Company
            Optional<Company> companyOpt = companyRepository.findTopByCountryCodeAndMobileOrderByIdDesc(countryCode, mobile);
            if (companyOpt.isEmpty() && !mobile.isEmpty()) {
                companyOpt = companyRepository.findTopByMobileOrderByIdDesc(mobile);
            }
            if (companyOpt.isEmpty() && !email.isEmpty()) {
                companyOpt = companyRepository.findByEmail(email);
            }

            String targetEmail = email;
            String targetName = "Valued Customer";

            if (companyOpt.isPresent()) {
                Company company = companyOpt.get();
                company.setOtp(newOtp);
                company.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
                company.setOtpVerified(false);
                companyRepository.save(company);
                targetEmail = (company.getEmail() != null && !company.getEmail().isEmpty()) ? company.getEmail() : email;
                targetName = company.getName();
                System.out.println("New OTP " + newOtp + " saved in MySQL for Company: " + company.getName());
            }

            // Send Resend OTP Email
            emailService.sendOtpEmail(targetEmail, targetName, newOtp);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "New OTP sent successfully to " + targetEmail);
            response.put("otp", newOtp);
            response.put("otpResponse", Map.of(
                    "otp", newOtp,
                    "message", "OTP sent successfully"
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Failed to resend OTP: " + e.getMessage()));
        }
    }

    private final Map<String, ResetTokenInfo> resetTokenCache = new java.util.concurrent.ConcurrentHashMap<>();

    static class ResetTokenInfo {
        private final String otp;
        private final LocalDateTime expiry;

        public ResetTokenInfo(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiry() {
            return expiry;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiry);
        }
    }

    // =====================================================
    // FORGOT PASSWORD
    // POST /api/auth/forgot-password
    // =====================================================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        System.out.println("=================================");
        System.out.println("FORGOT PASSWORD API CALLED");

        try {
            String email = request.getOrDefault("email", "").trim().toLowerCase();
            if (email.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email address is required"));
            }

            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                Optional<Company> compOpt = companyRepository.findByEmail(email);
                if (compOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("message", "No registered account found with this email address."));
                }
            }

            String userName = userOpt.map(User::getName).orElse("Valued User");
            String resetPin = String.format("%06d", new Random().nextInt(1000000));
            LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

            // Save to in-memory cache
            resetTokenCache.put(email, new ResetTokenInfo(resetPin, expiry));

            // Also store on Company if available
            if (userOpt.isPresent() && userOpt.get().getCompany() != null) {
                Company company = userOpt.get().getCompany();
                company.setOtp(resetPin);
                company.setOtpExpiry(expiry);
                companyRepository.save(company);
            } else {
                companyRepository.findByEmail(email).ifPresent(company -> {
                    company.setOtp(resetPin);
                    company.setOtpExpiry(expiry);
                    companyRepository.save(company);
                });
            }

            String resetLink = "http://localhost:4200/reset-password?email=" + email + "&token=" + resetPin;
            emailService.sendForgotPasswordEmail(email, userName, resetPin, resetLink);
            System.out.println("Password reset email dispatched to: " + email + " with PIN: " + resetPin);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "A 6-digit password reset code has been sent to " + email,
                    "email", email
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Failed to process forgot password request: " + e.getMessage()));
        }
    }

    // =====================================================
    // VERIFY RESET TOKEN
    // POST /api/auth/verify-reset-token
    // =====================================================
    @PostMapping("/verify-reset-token")
    public ResponseEntity<?> verifyResetToken(@RequestBody Map<String, String> request) {
        System.out.println("=================================");
        System.out.println("VERIFY RESET TOKEN API CALLED");

        try {
            String email = request.getOrDefault("email", "").trim().toLowerCase();
            String token = request.getOrDefault("token", request.getOrDefault("otp", "")).trim();

            if (email.isEmpty() || token.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email and reset token are required"));
            }

            boolean isValid = false;

            // 1. Check in-memory token cache
            ResetTokenInfo tokenInfo = resetTokenCache.get(email);
            if (tokenInfo != null && tokenInfo.getOtp().equals(token)) {
                if (!tokenInfo.isExpired()) {
                    isValid = true;
                } else {
                    return ResponseEntity.badRequest().body(Map.of("message", "Reset code has expired. Please request a new code."));
                }
            }

            // 2. Check company OTP in database as backup
            if (!isValid) {
                Optional<User> userOpt = userRepository.findByEmail(email);
                Company comp = userOpt.map(User::getCompany).orElse(null);
                if (comp == null) {
                    comp = companyRepository.findByEmail(email).orElse(null);
                }
                if (comp != null && token.equals(comp.getOtp())) {
                    if (comp.getOtpExpiry() != null && LocalDateTime.now().isBefore(comp.getOtpExpiry())) {
                        isValid = true;
                    } else {
                        return ResponseEntity.badRequest().body(Map.of("message", "Reset code has expired. Please request a new code."));
                    }
                }
            }

            if (!isValid) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid reset code. Please check your email and try again."));
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Reset code verified successfully!"
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Token verification failed: " + e.getMessage()));
        }
    }

    // =====================================================
    // RESET PASSWORD
    // POST /api/auth/reset-password
    // =====================================================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        System.out.println("=================================");
        System.out.println("RESET PASSWORD API CALLED");

        try {
            String email = request.getOrDefault("email", "").trim().toLowerCase();
            String token = request.getOrDefault("token", request.getOrDefault("otp", "")).trim();
            String newPassword = request.getOrDefault("newPassword", request.getOrDefault("password", ""));

            if (email.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }
            if (newPassword == null || newPassword.trim().length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("message", "New password must be at least 6 characters"));
            }

            Optional<User> userOpt = userRepository.findByEmail(email);
            Company comp = userOpt.map(User::getCompany).orElse(null);
            if (comp == null) {
                comp = companyRepository.findByEmail(email).orElse(null);
            }

            boolean isValid = false;

            if (token.isEmpty()) {
                // Direct Admin update from Dashboard User Management
                isValid = true;
            } else {
                // 1. Verify token in in-memory cache
                ResetTokenInfo tokenInfo = resetTokenCache.get(email);
                if (tokenInfo != null && tokenInfo.getOtp().equals(token)) {
                    if (!tokenInfo.isExpired()) {
                        isValid = true;
                    } else {
                        return ResponseEntity.badRequest().body(Map.of("message", "Reset code has expired. Please request a new one."));
                    }
                }

                // 2. Check company OTP in database as backup
                if (!isValid && comp != null && token.equals(comp.getOtp())) {
                    if (comp.getOtpExpiry() != null && LocalDateTime.now().isBefore(comp.getOtpExpiry())) {
                        isValid = true;
                    } else {
                        return ResponseEntity.badRequest().body(Map.of("message", "Reset code has expired. Please request a new one."));
                    }
                }

                if (!isValid) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Invalid verification code."));
                }
            }

            // Hash new password with BCrypt
            String encodedPassword = passwordEncoder.encode(newPassword.trim());

            // Update user password in MySQL users table
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setPassword(encodedPassword);
                userRepository.save(user);
                System.out.println("User password updated in MySQL for: " + email);
            }

            // Update company password and clear OTP
            if (comp != null) {
                comp.setPassword(encodedPassword);
                comp.setOtp(null);
                comp.setOtpExpiry(null);
                companyRepository.save(comp);
                System.out.println("Company password updated in MySQL for: " + comp.getName());
            }

            // Invalidate cache
            resetTokenCache.remove(email);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Password reset successfully! You can now sign in with your new password."
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Password reset failed: " + e.getMessage()));
        }
    }

    // =====================================================
    // SEND BILL INVOICE EMAIL
    // POST /api/auth/send-bill-email
    // =====================================================
    @PostMapping("/send-bill-email")
    public ResponseEntity<?> sendBillEmail(@RequestBody Map<String, Object> request) {
        System.out.println("=================================");
        System.out.println("SEND BILL EMAIL API CALLED");

        try {
            String toEmail = String.valueOf(request.getOrDefault("email", ""));
            String patientName = String.valueOf(request.getOrDefault("patientName", "Customer"));
            String invoiceNo = String.valueOf(request.getOrDefault("invoiceNo", "INV-" + System.currentTimeMillis()));
            String pharmacyName = String.valueOf(request.getOrDefault("pharmacyName", "MedBill Pharmacy"));
            String billDate = String.valueOf(request.getOrDefault("billDate", ""));
            Double totalAmount = Double.valueOf(String.valueOf(request.getOrDefault("totalAmount", "0.0")));
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");
            String notes = String.valueOf(request.getOrDefault("notes", ""));

            emailService.sendBillInvoiceEmail(toEmail, patientName, invoiceNo, pharmacyName, billDate, totalAmount, items, notes);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Medical bill invoice sent successfully to " + toEmail
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Failed to send bill email: " + e.getMessage()));
        }
    }

    // =====================================================
    // 2. VERIFY OTP
    // POST /api/auth/verify-otp
    // =====================================================
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        System.out.println("=================================");
        System.out.println("VERIFY OTP API CALLED");

        try {
            if (request == null || request.getOtp() == null || request.getOtp().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "OTP is required"));
            }

            String enteredOtp = request.getOtp().trim();
            String countryCode = request.getCountryCode() != null ? request.getCountryCode().trim() : "+91";
            String mobile = request.getMobile() != null ? request.getMobile().replaceAll("\\D", "") : "";
            String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";

            System.out.println("Verifying OTP for countryCode: " + countryCode + ", mobile: " + mobile + ", email: " + email + ", OTP: " + enteredOtp);

            // Find Company by countryCode and mobile, or by mobile, or by email
            Optional<Company> companyOpt = Optional.empty();
            if (!mobile.isEmpty()) {
                companyOpt = companyRepository.findTopByCountryCodeAndMobileOrderByIdDesc(countryCode, mobile);
                if (companyOpt.isEmpty()) {
                    companyOpt = companyRepository.findTopByMobileOrderByIdDesc(mobile);
                }
            }
            if (companyOpt.isEmpty() && !email.isEmpty()) {
                companyOpt = companyRepository.findByEmail(email);
            }

            if (companyOpt.isEmpty()) {
                System.out.println("Company not found for mobile: " + mobile + " / email: " + email);
                return ResponseEntity.badRequest().body(Map.of("message", "Company record not found for this account."));
            }

            Company company = companyOpt.get();

            // Validate OTP Match
            if (company.getOtp() == null || !company.getOtp().equals(enteredOtp)) {
                System.out.println("Invalid OTP! Expected: " + company.getOtp() + ", Received: " + enteredOtp);
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid OTP. Please enter the correct code."));
            }

            // Check Expiry
            if (company.getOtpExpiry() != null && LocalDateTime.now().isAfter(company.getOtpExpiry())) {
                System.out.println("OTP Expired for company ID: " + company.getId());
                return ResponseEntity.badRequest().body(Map.of("message", "OTP has expired. Please request a new code."));
            }

            // Mark Verified and clear OTP in MySQL companies table
            company.setOtpVerified(true);
            company.setStatus("ACTIVE");
            company.setOtp(null);
            company.setOtpExpiry(null);
            companyRepository.save(company);

            // Activate associated users in MySQL users table so they can now login!
            List<User> companyUsers = userRepository.findByCompanyId(company.getId());
            for (User u : companyUsers) {
                u.setActive(true);
                userRepository.save(u);
                System.out.println("User account activated in MySQL: " + u.getEmail());
            }

            if (company.getEmail() != null && !company.getEmail().isEmpty()) {
                Optional<User> adminOpt = userRepository.findByEmail(company.getEmail().trim().toLowerCase());
                if (adminOpt.isPresent()) {
                    User admin = adminOpt.get();
                    admin.setActive(true);
                    userRepository.save(admin);
                    System.out.println("Admin account activated in MySQL: " + admin.getEmail());
                }
            }

            System.out.println("OTP verified & Company/User accounts activated in MySQL for Company ID: " + company.getId());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "OTP verified successfully! You can now sign in.",
                    "companyId", company.getId()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid OTP. Please check the code and try again."));
        }
    }



    // =====================================================
    // 4. LOGIN
    // POST /api/auth/login
    // =====================================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("=================================");
        System.out.println("LOGIN API CALLED: Email=" + request.getEmail() + ", Store=" + request.getStoreName());

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email address is required"));
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
        }

        String email = request.getEmail().trim().toLowerCase();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid Email or Password"));
        }

        User user = userOpt.get();

        // 1. Password Verification
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid Email or Password"));
        }

        // 2. CHECK OTP VERIFICATION STATUS (BLOCK LOGIN IF NOT VERIFIED)
        Company company = user.getCompany();
        if (company != null && Boolean.FALSE.equals(company.getOtpVerified())) {
            System.out.println("Login blocked: OTP not verified for Company: " + company.getName() + " (" + user.getEmail() + ")");
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Your account is not verified yet! Please complete OTP verification to log in.",
                "notVerified", true,
                "email", user.getEmail(),
                "mobile", company.getMobile() != null ? company.getMobile() : "",
                "countryCode", company.getCountryCode() != null ? company.getCountryCode() : "+91",
                "companyName", company.getName() != null ? company.getName() : ""
            ));
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
            System.out.println("Login blocked: User account inactive / unverified: " + user.getEmail());
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Your account is not verified yet! Please complete OTP verification to log in.",
                "notVerified", true,
                "email", user.getEmail()
            ));
        }

        boolean isSuperAdmin = "admin@gmail.com".equalsIgnoreCase(email) || "SUPER_ADMIN".equalsIgnoreCase(user.getRole());
        if (isSuperAdmin && !"SUPER_ADMIN".equalsIgnoreCase(user.getRole())) {
            user.setRole("SUPER_ADMIN");
            userRepository.save(user);
        }

        // Validate Store / Pharmacy Name if provided (Bypass for Super Admin admin@gmail.com)
        if (request.getStoreName() != null && !request.getStoreName().trim().isEmpty()) {
            String enteredStore = request.getStoreName().trim();
            if (isSuperAdmin) {
                // If Super Admin enters a store name, switch context to that store
                var allComps = companyRepository.findAll();
                for (Company c : allComps) {
                    if (c.getName() != null && (c.getName().equalsIgnoreCase(enteredStore) 
                            || c.getName().toLowerCase().contains(enteredStore.toLowerCase())
                            || enteredStore.toLowerCase().contains(c.getName().toLowerCase()))) {
                        company = c;
                        break;
                    }
                }
            } else {
                Company comp = user.getCompany();
                if (comp != null && comp.getName() != null) {
                    String existingStore = comp.getName().trim();
                    boolean matches = existingStore.equalsIgnoreCase(enteredStore) 
                            || existingStore.toLowerCase().contains(enteredStore.toLowerCase())
                            || enteredStore.toLowerCase().contains(existingStore.toLowerCase());
                    if (!matches) {
                        return ResponseEntity.badRequest().body(Map.of("message", "Invalid Store Name: '" + enteredStore + "' does not match your registered store ('" + existingStore + "')"));
                    }
                }
            }
        }

        String token = jwtService.generateToken(user);

        LoginResponse response = LoginResponse.builder()
                .userName(user.getName())
                .role(isSuperAdmin ? "SUPER_ADMIN" : (user.getRole() != null ? user.getRole() : "ADMIN"))
                .token(token)
                .companyId(company != null ? company.getId() : null)
                .companyName(company != null ? company.getName() : null)
                .build();

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // 5. GET ALL USERS (FOR USER MANAGEMENT /users)
    // GET /api/auth/users
    // =====================================================
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "companyId", required = false) Long paramCompanyId) {
        System.out.println("=================================");
        System.out.println("GET ALL USERS API CALLED. Param companyId: " + paramCompanyId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        }

        Long targetCompanyId = paramCompanyId;
        if (targetCompanyId == null && currentUser != null && currentUser.getCompany() != null) {
            String currentRole = currentUser.getRole() != null ? currentUser.getRole().toUpperCase() : "";
            if (!currentRole.contains("SUPER_ADMIN") && !currentRole.contains("SUPERADMIN")) {
                targetCompanyId = currentUser.getCompany().getId();
            }
        }

        List<User> users;
        if (targetCompanyId != null) {
            users = userRepository.findByCompanyId(targetCompanyId);
        } else {
            users = userRepository.findAll();
        }

        List<Map<String, Object>> responseList = new ArrayList<>();

        for (User u : users) {
            String uEmail = u.getEmail() != null ? u.getEmail().trim().toLowerCase() : "";
            String role = u.getRole() != null ? u.getRole().trim().toUpperCase() : "";

            // HIDE ONLY SUPER ADMIN (admin@gmail.com) FROM USER MANAGEMENT LIST
            if ("admin@gmail.com".equalsIgnoreCase(uEmail) 
                    || "SUPER_ADMIN".equalsIgnoreCase(role) 
                    || "SUPERADMIN".equalsIgnoreCase(role)) {
                continue;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("name", u.getName());
            map.put("userName", u.getName());
            map.put("email", u.getEmail());
            map.put("role", u.getRole() != null ? u.getRole() : "ADMIN");
            map.put("active", u.getActive());
            map.put("status", Boolean.TRUE.equals(u.getActive()) ? "Active" : "Inactive");
            if (u.getCompany() != null) {
                map.put("companyId", u.getCompany().getId());
                map.put("companyName", u.getCompany().getName());
                map.put("mobile", u.getCompany().getMobile());
                map.put("countryCode", u.getCompany().getCountryCode());
            } else {
                map.put("companyId", null);
                map.put("companyName", "MediCare Hospital");
                map.put("mobile", "");
                map.put("countryCode", "+91");
            }
            responseList.add(map);
        }

        return ResponseEntity.ok(responseList);
    }

    // =====================================================
    // 6. UPDATE USER
    // PUT /api/auth/users/{email}
    // =====================================================
    @PutMapping("/users/{email}")
    public ResponseEntity<?> updateUser(@PathVariable("email") String email, @RequestBody Map<String, Object> req) {
        System.out.println("=================================");
        System.out.println("UPDATE USER API CALLED FOR: " + email);

        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8).trim().toLowerCase();
            Optional<User> userOpt = userRepository.findByEmail(decodedEmail);

            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "User not found with email: " + decodedEmail));
            }

            User user = userOpt.get();
            if (req.containsKey("userName") || req.containsKey("name")) {
                String name = String.valueOf(req.getOrDefault("userName", req.get("name")));
                if (name != null && !name.trim().isEmpty()) {
                    user.setName(name.trim());
                }
            }

            if (req.containsKey("role")) {
                user.setRole(String.valueOf(req.get("role")));
            }

            if (req.containsKey("active")) {
                user.setActive(Boolean.valueOf(String.valueOf(req.get("active"))));
            } else if (req.containsKey("status")) {
                user.setActive("Active".equalsIgnoreCase(String.valueOf(req.get("status"))));
            }

            userRepository.save(user);
            return ResponseEntity.ok(Map.of("success", true, "message", "User updated successfully in MySQL"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Failed to update user: " + e.getMessage()));
        }
    }

    // =====================================================
    // 7. DELETE USER
    // DELETE /api/auth/users/{email}
    // =====================================================
    @DeleteMapping("/users/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        System.out.println("=================================");
        System.out.println("DELETE USER API CALLED FOR: " + email);

        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8).trim().toLowerCase();
            Optional<User> userOpt = userRepository.findByEmail(decodedEmail);

            if (userOpt.isPresent()) {
                userRepository.delete(userOpt.get());
                System.out.println("User deleted from MySQL users table: " + decodedEmail);
                return ResponseEntity.ok(Map.of("success", true, "message", "User deleted successfully"));
            }

            return ResponseEntity.ok(Map.of("success", true, "message", "User not found or already removed"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Failed to delete user: " + e.getMessage()));
        }
    }
}