package com.medbill.dto;

public class VerifyOtpRequest {

    private String countryCode;
    private String mobile;
    private String email;
    private String otp;

    // ================= GETTERS =================

    public String getCountryCode() {
        return countryCode;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    // ================= SETTERS =================

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}