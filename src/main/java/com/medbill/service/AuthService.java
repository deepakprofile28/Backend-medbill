package com.medbill.service;

import com.medbill.dto.LoginRequest;
import com.medbill.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}