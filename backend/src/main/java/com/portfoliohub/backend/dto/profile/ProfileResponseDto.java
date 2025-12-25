package com.portfoliohub.backend.dto.profile;

import java.time.LocalDate;

public record ProfileResponseDto(
        Long id,
        String username,
        String fullName,
        String bio,
        String avatarUrl,
        String location,
        String website,
        LocalDate birthdate
) {}
