package com.medbill.service;

import com.medbill.dto.LoginRequest;
import com.medbill.dto.LoginResponse;
import com.medbill.dto.RegisterRequest;
import com.medbill.dto.VerifyOtpRequest;

public interface AuthService {

    // ================= REGISTER =================

    String register(RegisterRequest request);

    // ================= VERIFY OTP =================

    String verifyOtp(VerifyOtpRequest request);

    // ================= LOGIN =================

    LoginResponse login(LoginRequest request);
}