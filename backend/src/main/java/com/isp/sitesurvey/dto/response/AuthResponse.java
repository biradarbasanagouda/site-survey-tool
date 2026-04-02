package com.isp.sitesurvey.dto.response;
public record AuthResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn,
    UserResponse user
) {
    public static AuthResponse of(String access, String refresh, Long expiry, UserResponse user) {
        return new AuthResponse(access, refresh, "Bearer", expiry, user);
    }
}