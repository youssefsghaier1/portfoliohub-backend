package com.portfoliohub.backend.controller;

import com.portfoliohub.backend.dto.auth.AuthResponse;
import com.portfoliohub.backend.dto.auth.LoginRequest;
import com.portfoliohub.backend.dto.auth.RefreshRequest;
import com.portfoliohub.backend.dto.auth.RegisterRequest;
import com.portfoliohub.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")

    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    //@PostMapping("/login")
    //public ResponseEntity<AuthResponse> authenticate(
            //@Valid @RequestBody LoginRequest request
    //) {
      //  return ResponseEntity.ok(authService.authenticate(request));
    //}

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}