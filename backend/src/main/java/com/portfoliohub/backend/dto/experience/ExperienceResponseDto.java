package com.portfoliohub.backend.dto.experience;

import java.time.LocalDate;

public record ExperienceResponseDto(
        Long id,
        String company,
        String position,
        String description,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isCurrent
) {}
