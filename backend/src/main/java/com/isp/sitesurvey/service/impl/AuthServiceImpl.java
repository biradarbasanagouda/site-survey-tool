package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.dto.request.*;
import com.isp.sitesurvey.dto.response.*;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.*;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.security.JwtUtil;
import com.isp.sitesurvey.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.access-token-expiry}")
    private long accessTokenExpiry;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new ResourceNotFoundException("User", 0L));

        refreshTokenRepository.revokeAllByUserId(user.getId());

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        refreshTokenRepository.save(RefreshToken.builder()
            .user(user)
            .token(refreshToken)
            .expiresAt(LocalDateTime.now().plusDays(7))
            .build());

        return AuthResponse.of(accessToken, refreshToken, accessTokenExpiry / 1000,
            UserResponse.from(user));
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already registered: " + request.email());
        }
        User user = User.builder()
            .email(request.email())
            .fullName(request.fullName())
            .passwordHash(passwordEncoder.encode(request.password()))
            .build();
        userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        refreshTokenRepository.save(RefreshToken.builder()
            .user(user)
            .token(refreshToken)
            .expiresAt(LocalDateTime.now().plusDays(7))
            .build());

        return AuthResponse.of(accessToken, refreshToken, accessTokenExpiry / 1000,
            UserResponse.from(user));
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken rt = refreshTokenRepository.findByToken(request.refreshToken())
            .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        if (rt.getRevoked() || rt.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Refresh token expired or revoked");
        }

        User user = rt.getUser();
        rt.setRevoked(true);
        refreshTokenRepository.save(rt);

        String newAccess = jwtUtil.generateAccessToken(user.getEmail(), user.getId());
        String newRefresh = jwtUtil.generateRefreshToken(user.getEmail());

        refreshTokenRepository.save(RefreshToken.builder()
            .user(user).token(newRefresh)
            .expiresAt(LocalDateTime.now().plusDays(7))
            .build());

        return AuthResponse.of(newAccess, newRefresh, accessTokenExpiry / 1000,
            UserResponse.from(user));
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
            .ifPresent(rt -> { rt.setRevoked(true); refreshTokenRepository.save(rt); });
    }
}