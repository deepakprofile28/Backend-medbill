package com.medbill.dto;

public class RegisterRequest {

    private String name;

    private String email;

    private String password;

    private String role;

    private boolean active;

    // ================= COMPANY =================

    private Long companyId;

    // ================= PHONE =================

    private String countryCode;

    private String mobile;

    private String companyName;

    // ================= GETTERS =================

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getMobile() {
        return mobile;
    }

    // ================= SETTERS =================

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}