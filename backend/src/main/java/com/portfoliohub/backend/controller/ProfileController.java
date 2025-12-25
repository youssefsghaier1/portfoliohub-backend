package com.portfoliohub.backend.controller;

import com.portfoliohub.backend.dto.profile.ProfileResponseDto;
import com.portfoliohub.backend.dto.profile.ProfileUpdateRequestDto;
import com.portfoliohub.backend.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // 🔐 Get my profile
    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDto> getMyProfile(
            Authentication authentication
    ) {
        return ResponseEntity.ok(profileService.getMyProfile(authentication));
    }

    // 🔐 Update my profile
    @PutMapping("/me")
    public ResponseEntity<ProfileResponseDto> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileUpdateRequestDto request
    ) {
        return ResponseEntity.ok(
                profileService.updateMyProfile(authentication, request)
        );
    }

    // 🌍 Public profile by username
    @GetMapping("/{username}")
    public ResponseEntity<ProfileResponseDto> getPublicProfile(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(profileService.getPublicProfile(username));
    }
}
