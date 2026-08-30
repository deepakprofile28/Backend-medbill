package com.medbill.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.medbill.entity.Company;
import com.medbill.entity.User;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.UserRepository;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:4200")
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyController(
            CompanyRepository companyRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =====================================================
    // GET ALL COMPANIES
    // GET /api/companies
    // =====================================================
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyRepository.findAll());
    }

    // =====================================================
    // GET COMPANY BY ID
    // GET /api/companies/{id}
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return ResponseEntity.ok(company.get());
        }
        return ResponseEntity.notFound().build();
    }

    // =====================================================
    // CREATE COMPANY DIRECTLY
    // POST /api/companies
    // =====================================================
    @PostMapping
    public ResponseEntity<?> createCompany(@RequestBody Company company) {
        try {
            if (company.getName() == null || company.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Company name is required"));
            }

            if (company.getStatus() == null || company.getStatus().trim().isEmpty()) {
                company.setStatus("ACTIVE");
            }

            if (company.getCreatedAt() == null) {
                company.setCreatedAt(LocalDateTime.now());
            }

            if (company.getPassword() != null && !company.getPassword().trim().isEmpty()) {
                company.setPassword(passwordEncoder.encode(company.getPassword()));
            }

            Company savedCompany = companyRepository.save(company);

            // If email is provided, ensure an Admin User exists for this company
            if (company.getEmail() != null && !company.getEmail().trim().isEmpty()) {
                String email = company.getEmail().trim().toLowerCase();
                Optional<User> existingUser = userRepository.findByEmail(email);
                if (existingUser.isEmpty()) {
                    User admin = new User();
                    admin.setName(company.getName());
                    admin.setEmail(email);
                    admin.setPassword(company.getPassword() != null ? company.getPassword() : passwordEncoder.encode("admin123"));
                    admin.setRole("ADMIN");
                    admin.setActive(true);
                    admin.setCompany(savedCompany);
                    userRepository.save(admin);
                }
            }

            return ResponseEntity.ok(savedCompany);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Failed to create company: " + e.getMessage()));
        }
    }

    // =====================================================
    // UPDATE COMPANY
    // PUT /api/companies/{id}
    // =====================================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody Company updatedCompany) {
        return companyRepository.findById(id).map(existing -> {
            if (updatedCompany.getName() != null) existing.setName(updatedCompany.getName());
            if (updatedCompany.getEmail() != null) existing.setEmail(updatedCompany.getEmail());
            if (updatedCompany.getMobile() != null) existing.setMobile(updatedCompany.getMobile());
            if (updatedCompany.getCountryCode() != null) existing.setCountryCode(updatedCompany.getCountryCode());
            if (updatedCompany.getAddress() != null) existing.setAddress(updatedCompany.getAddress());
            if (updatedCompany.getStatus() != null) existing.setStatus(updatedCompany.getStatus());
            if (updatedCompany.getPlan() != null) existing.setPlan(updatedCompany.getPlan());
            if (updatedCompany.getRenewalDate() != null) existing.setRenewalDate(updatedCompany.getRenewalDate());
            Company saved = companyRepository.save(existing);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // =====================================================
    // DELETE COMPANY
    // DELETE /api/companies/{id}
    // =====================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Company deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }
}

