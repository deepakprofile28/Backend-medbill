package com.medbill.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "companies")
public class Company {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // COMPANY NAME
    // =====================================================

    @Column(nullable = false, length = 150)
    private String name;

    // =====================================================
    // COMPANY EMAIL
    // =====================================================

    @Column(length = 150)
    private String email;

    // =====================================================
    // COUNTRY CODE
    // =====================================================

    @Column(name = "country_code", length = 10)
    private String countryCode;

    // =====================================================
    // COMPANY MOBILE
    // =====================================================

    @Column(name = "mobile",length = 20)
    private String mobile;

    // =====================================================
    // COMPANY ADDRESS
    // =====================================================

    @Column(length = 255)
    private String address;

    // =====================================================
    // COMPANY STATUS
    // =====================================================

    @Column(length = 20)
    private String status;

    // =====================================================
    // CREATED DATE
    // =====================================================

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // =====================================================
    // PASSWORD
    // =====================================================

    @Column(length = 255)
    private String password;

    // =====================================================
    // RENEWAL DATE
    // =====================================================

    @Column(name = "renewal_date")
    private LocalDate renewalDate;

    // =====================================================
    // PLAN
    // =====================================================

    @Column(length = 50)
    private String plan;

    // =====================================================
    // OTP
    // =====================================================

    @Column(length = 10)
    private String otp;

    // =====================================================
    // OTP EXPIRY
    // =====================================================

    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;

    // =====================================================
    // OTP VERIFIED
    // =====================================================

    @Column(name = "otp_verified")
    private Boolean otpVerified;

    // =====================================================
    // DEFAULT VALUES
    // =====================================================

    @PrePersist
    protected void onCreate() {

        if (status == null || status.trim().isEmpty()) {
            status = "ACTIVE";
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (plan == null || plan.trim().isEmpty()) {
            plan = "BASIC";
        }

        if (otpVerified == null) {
            otpVerified = false;
        }
    }

    // =====================================================
    // GETTERS / SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public Boolean getOtpVerified() {
        return otpVerified;
    }

    public void setOtpVerified(Boolean otpVerified) {
        this.otpVerified = otpVerified;
    }
}