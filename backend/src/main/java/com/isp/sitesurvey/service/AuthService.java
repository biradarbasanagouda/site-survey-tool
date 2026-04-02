package com.isp.sitesurvey.service;
import com.isp.sitesurvey.dto.request.*;
import com.isp.sitesurvey.dto.response.AuthResponse;
public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(String refreshToken);
}