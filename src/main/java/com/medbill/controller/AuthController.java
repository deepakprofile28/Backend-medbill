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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String resetPin = String.format("%06d", new Random().nextInt(1000000));

                if (user.getCompany() != null) {
                    Company company = user.getCompany();
                    company.setOtp(resetPin);
                    company.setOtpExpiry(LocalDateTime.now().plusMinutes(15));
                    companyRepository.save(company);
                }

                emailService.sendForgotPasswordEmail(email, user.getName(), resetPin, "http://localhost:4200/login");
                System.out.println("Password reset email dispatched to: " + email);
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "If an account exists with this email, password reset instructions have been sent to your Gmail."
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Failed to process forgot password request: " + e.getMessage()));
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
    // 3. REGISTER STAFF / USER
    // POST /api/auth/register
    // =====================================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        System.out.println("=================================");
        System.out.println("REGISTER STAFF API CALLED");

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Name is required"));
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
        }

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
        }

        Company company = null;
        if (request.getCompanyId() != null) {
            company = companyRepository.findById(request.getCompanyId()).orElse(null);
        }

        // If company not found, find the first available or create default
        if (company == null) {
            var allCompanies = companyRepository.findAll();
            if (!allCompanies.isEmpty()) {
                company = allCompanies.get(0);
            } else {
                Company defaultComp = new Company();
                defaultComp.setName(request.getName() + " Pharmacy");
                defaultComp.setEmail(email);
                defaultComp.setStatus("ACTIVE");
                defaultComp.setCreatedAt(LocalDateTime.now());
                company = companyRepository.save(defaultComp);
            }
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "ADMIN");
        user.setActive(request.isActive());
        user.setCompany(company);

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User registered successfully",
                "userId", savedUser.getId(),
                "companyId", company.getId()
        ));
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
                .role(user.getRole())
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
    public ResponseEntity<?> getAllUsers() {
        System.out.println("=================================");
        System.out.println("GET ALL USERS API CALLED");

        List<User> users = userRepository.findAll();
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

    // =====================================================
    // 8. RESET USER PASSWORD
    // POST /api/auth/reset-password
    // =====================================================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {
        System.out.println("=================================");
        System.out.println("RESET PASSWORD API CALLED FOR: " + req.get("email"));

        try {
            String email = req.getOrDefault("email", "").trim().toLowerCase();
            String newPassword = req.getOrDefault("newPassword", "");

            if (email.isEmpty() || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email and new password are required"));
            }

            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                System.out.println("Password reset in MySQL for user: " + email);
                return ResponseEntity.ok(Map.of("success", true, "message", "Password reset successfully"));
            }

            return ResponseEntity.badRequest().body(Map.of("message", "User not found with email: " + email));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Failed to reset password: " + e.getMessage()));
        }
    }
}