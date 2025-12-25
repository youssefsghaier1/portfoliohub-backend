package com.portfoliohub.backend.dto.project;

import java.time.LocalDate;

public record ProjectResponseDto(
        Long id,
        String title,
        String description,
        String githubUrl,
        String liveDemoUrl,
        String imageUrl,
        Boolean featured,
        LocalDate startDate,
        LocalDate endDate
) {}

