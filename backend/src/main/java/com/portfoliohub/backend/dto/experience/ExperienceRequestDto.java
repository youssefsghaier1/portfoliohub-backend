package com.portfoliohub.backend.dto.experience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExperienceRequestDto(
        @NotBlank String company,
        @NotBlank String position,
        String description,
        String location,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        Boolean isCurrent
) {}
