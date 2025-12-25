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

    //  PUBLIC profile
    public ProfileResponseDto getPublicProfile(String username) {
        Profile profile = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        return mapToDto(profile);
    }

    // Mapper (manual = explicit & clean)
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
}
