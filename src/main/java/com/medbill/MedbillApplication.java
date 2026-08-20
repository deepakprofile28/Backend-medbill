package com.medbill;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.medbill.entity.User;
import com.medbill.repository.UserRepository;

@SpringBootApplication
public class MedbillApplication {

    public static void main(String[] args) {

        SpringApplication.run(MedbillApplication.class, args);
    }

    @Bean
    CommandLineRunner createTestUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                User user = new User();

                user.setName("Admin");
                user.setEmail("admin@gmail.com");
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setRole("ADMIN");
                user.setActive(true);

                userRepository.save(user);

                System.out.println("=================================");
                System.out.println("Test user created successfully!");
                System.out.println("Email    : admin@gmail.com");
                System.out.println("Password : admin123");
                System.out.println("=================================");
            }
        };
    }
}