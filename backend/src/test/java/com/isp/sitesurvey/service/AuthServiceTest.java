package com.isp.sitesurvey.service;

import com.isp.sitesurvey.dto.request.LoginRequest;
import com.isp.sitesurvey.dto.request.RegisterRequest;
import com.isp.sitesurvey.dto.response.AuthResponse;
import com.isp.sitesurvey.entity.User;
import com.isp.sitesurvey.exception.BadRequestException;
import com.isp.sitesurvey.repository.RefreshTokenRepository;
import com.isp.sitesurvey.repository.UserRepository;
import com.isp.sitesurvey.security.JwtUtil;
import com.isp.sitesurvey.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock RefreshTokenRepository refreshTokenRepository;
    @Mock JwtUtil jwtUtil;
    @Mock PasswordEncoder passwordEncoder;
    @Mock AuthenticationManager authenticationManager;

    @InjectMocks AuthServiceImpl authService;

    @BeforeEach
    void setup() {
        // inject access-token-expiry via reflection
        try {
            var field = AuthServiceImpl.class.getDeclaredField("accessTokenExpiry");
            field.setAccessible(true);
            field.set(authService, 900000L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void login_success() {
        User user = User.builder().id(1L).email("admin@isp.com")
            .fullName("Admin").passwordHash("hashed").build();

        when(authenticationManager.authenticate(any())).thenReturn(mock(org.springframework.security.core.Authentication.class));
        when(userRepository.findByEmail("admin@isp.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateAccessToken(anyString(), anyLong())).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("refresh-token");
        doNothing().when(refreshTokenRepository).revokeAllByUserId(anyLong());
        when(refreshTokenRepository.save(any())).thenReturn(null);

        AuthResponse response = authService.login(new LoginRequest("admin@isp.com", "Admin@123"));

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        assertThat(response.user().email()).isEqualTo("admin@isp.com");
    }

    @Test
    void register_duplicateEmail_throwsBadRequest() {
        when(userRepository.existsByEmail("dup@isp.com")).thenReturn(true);

        assertThatThrownBy(() ->
            authService.register(new RegisterRequest("dup@isp.com", "Test User", "Password1!"))
        ).isInstanceOf(BadRequestException.class)
         .hasMessageContaining("already registered");
    }

    @Test
    void register_newUser_returnsTokens() {
        when(userRepository.existsByEmail("new@isp.com")).thenReturn(false);
        User saved = User.builder().id(2L).email("new@isp.com").fullName("New User").passwordHash("hashed").build();
        when(userRepository.save(any())).thenReturn(saved);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(jwtUtil.generateAccessToken(anyString(), anyLong())).thenReturn("at");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("rt");
        when(refreshTokenRepository.save(any())).thenReturn(null);

        AuthResponse response = authService.register(new RegisterRequest("new@isp.com", "New User", "Password1!"));
        assertThat(response.user().email()).isEqualTo("new@isp.com");
    }
}
