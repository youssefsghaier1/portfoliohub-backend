package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.dto.profile.ProfileResponseDto;
import com.portfoliohub.backend.dto.profile.ProfileUpdateRequestDto;
import com.portfoliohub.backend.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileCompletionService profileCompletionService; // ✅ ADD THIS

    // 🔐 GET my profile
    public ProfileResponseDto getMyProfile(Authentication authentication) {
        String email = authentication.getName();

        Profile profile = profileRepository.findByUser_Email(email)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        return mapToDto(profile);
    }

    // 🔐 UPDATE my profile
    @Transactional
    public ProfileResponseDto updateMyProfile(
            Authentication authentication,
            ProfileUpdateRequestDto request
    ) {
        String email = authentication.getName();

        Profile profile = profileRepository.findByUser_Email(email)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        profile.setFullName(request.fullName());
        profile.setBio(request.bio());
        profile.setAvatarUrl(request.avatarUrl());
        profile.setLocation(request.location());
        profile.setWebsite(request.website());
        profile.setBirthdate(request.birthdate());

        return mapToDto(profile);
    }

    // 🌍 Public profile (basic info only)
    public ProfileResponseDto getPublicProfile(String username) {
        Profile profile = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        return mapToDto(profile);
    }

    // ⭐ GET completion score
    public int getCompletionScore(Authentication auth) {
        Profile profile = profileRepository.findByUser_Email(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        return profileCompletionService.calculateScore(profile);
    }

    // ======================
    // Mapper
    // ======================
    private ProfileResponseDto mapToDto(Profile profile) {
        return new ProfileResponseDto(
                profile.getId(),
                profile.getUser().getUsername(),
                profile.getFullName(),
                profile.getBio(),
                profile.getAvatarUrl(),
                profile.getLocation(),
                profile.getWebsite(),
                profile.getBirthdate()
        );
    }
    @Transactional
    public void updateVisibility(Authentication auth, boolean isPublic) {
        Profile profile = profileRepository.findByUser_Email(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setPublicProfile(isPublic);
    }
}
