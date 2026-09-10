package com.medbill.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // ================= PASSWORD ENCODER =================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ================= CORS CONFIGURATION =================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:4200")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type"
                )
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // ================= SECURITY CONFIGURATION =================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            // Enable CORS
            .cors(cors -> cors.configurationSource(
                    corsConfigurationSource()
            ))

            // Disable CSRF
            .csrf(csrf -> csrf.disable())

            // Disable form login
            .formLogin(form -> form.disable())

            // Disable HTTP Basic
            .httpBasic(basic -> basic.disable())

            // JWT → Stateless
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // Authorization
            .authorizeHttpRequests(auth -> auth

                // Allow CORS preflight
                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                ).permitAll()

                // Public Auth, Company, Patient, Medicine, and AI APIs
                .requestMatchers(
                    "/api/auth/**",
                    "/api/companies/**",
                    "/api/patients/**",
                    "/api/medicines/**",
                    "/api/ai/**",
                    "/api/store-config/**",
                    "/api/taxes/**",
                    "/api/categories/**",
                    "/api/roles/**",
                    "/api/access/**",
                    "/api/doctors/**",
                    "/api/warehouses/**",
                    "/api/brands/**",
                    "/api/branches/**",
                    "/api/sales/**",
                    "/api/suppliers/**",
                    "/api/purchases/**",
                    "/api/purchase-returns/**",
                    "/api/racks/**"
                ).permitAll()
                .anyRequest().authenticated()
            )

            // JWT Filter
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}