package com.isp.sitesurvey.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {

    JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",
            "test-super-secret-jwt-key-minimum-256-bits-long-string");
        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpiry",  900_000L);
        ReflectionTestUtils.setField(jwtUtil, "refreshTokenExpiry", 604_800_000L);
    }

    @Test
    void generateAndValidateAccessToken() {
        String token = jwtUtil.generateAccessToken("user@test.com", 42L);
        assertThat(token).isNotBlank();
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
        assertThat(jwtUtil.extractEmail(token)).isEqualTo("user@test.com");
    }

    @Test
    void generateRefreshToken_isValid() {
        String token = jwtUtil.generateRefreshToken("user@test.com");
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
    }

    @Test
    void invalidToken_returnsFalse() {
        assertThat(jwtUtil.isTokenValid("not.a.valid.jwt")).isFalse();
    }

    @Test
    void tamperedToken_returnsFalse() {
        String token = jwtUtil.generateAccessToken("user@test.com", 1L);
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";
        assertThat(jwtUtil.isTokenValid(tampered)).isFalse();
    }
}
