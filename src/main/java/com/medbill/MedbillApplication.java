package com.medbill;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.medbill.entity.Company;
import com.medbill.entity.User;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.UserRepository;

@SpringBootApplication
public class MedbillApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                MedbillApplication.class,
                args
        );
    }

    @Bean
    CommandLineRunner createTestUser(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // =================================================
            // FIND COMPANY
            // =================================================

            Company company =
                    companyRepository.findById(1L)
                            .orElse(null);

            if (company == null) {
                company = new Company();
                company.setName("Apollo Medicals");
                company.setEmail("admin@medicalbilling.com");
                company.setMobile("9876543210");
                company.setCountryCode("+91");
                company.setAddress("Chennai, Tamil Nadu");
                company.setStatus("ACTIVE");
                company.setOtpVerified(true);
                company.setCreatedAt(java.time.LocalDateTime.now());
                company = companyRepository.save(company);
                System.out.println("Default Store Company created with ID: " + company.getId() + " (" + company.getName() + ")");
            }

            // =================================================
            // CREATE OR MIGRATE SUPER ADMIN USER (admin@gmail.com)
            // =================================================

            var adminOpt = userRepository.findByEmail("admin@gmail.com");
            if (adminOpt.isEmpty()) {
                User user = new User();
                user.setName("Admin");
                user.setEmail("admin@gmail.com");
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setRole("SUPER_ADMIN");
                user.setActive(true);
                user.setCompany(company);

                userRepository.save(user);

                System.out.println("=================================");
                System.out.println("Super Admin created successfully!");
                System.out.println("Email      : admin@gmail.com");
                System.out.println("Role       : SUPER_ADMIN");
                System.out.println("=================================");
            } else {
                User existingUser = adminOpt.get();
                if (!"SUPER_ADMIN".equalsIgnoreCase(existingUser.getRole())) {
                    existingUser.setRole("SUPER_ADMIN");
                    if (existingUser.getCompany() == null && company != null) {
                        existingUser.setCompany(company);
                    }
                    userRepository.save(existingUser);
                    System.out.println("=================================");
                    System.out.println("Migrated admin@gmail.com to SUPER_ADMIN successfully!");
                    System.out.println("=================================");
                } else {
                    System.out.println("Super Admin user admin@gmail.com verified (SUPER_ADMIN).");
                }
            }
        };
    }
}