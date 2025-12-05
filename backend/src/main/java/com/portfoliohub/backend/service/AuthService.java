package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.RefreshToken;
import com.portfoliohub.backend.dto.auth.AuthResponse;
import com.portfoliohub.backend.dto.auth.LoginRequest;
import com.portfoliohub.backend.dto.auth.RefreshRequest;
import com.portfoliohub.backend.dto.auth.RegisterRequest;
import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.Entities.User;
import com.portfoliohub.backend.repository.RefreshTokenRepository;
import com.portfoliohub.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.portfoliohub.backend.repository.ProfileRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ProfileRepository profileRepository;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .createdAt(LocalDateTime.now())  // <-- REQUIRED
                .build();

        userRepository.save(user);

        // Create profile
        Profile profile = Profile.builder()
                .user(user)
                .fullName(request.getFullName())
                .createdAt(LocalDateTime.now())
                .build();

        profileRepository.save(profile);

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken(), "Bearer");
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken(), "Bearer");
    }

    private RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.deleteByUser(user);

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(jwtService.generateRefreshToken(user))
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(token);
    }

    public AuthResponse refreshToken(RefreshRequest request) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.isExpired() || refreshToken.getRevoked()) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        User user = refreshToken.getUser();
        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(newAccessToken, refreshToken.getToken(), "Bearer");
    }
}
