package com.portfoliohub.backend.dto.profile;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record ProfileUpdateRequestDto(
        @NotBlank
        String fullName,
        String bio,
        String avatarUrl,
        String location,
        String website,
        LocalDate birthdate
) {}
