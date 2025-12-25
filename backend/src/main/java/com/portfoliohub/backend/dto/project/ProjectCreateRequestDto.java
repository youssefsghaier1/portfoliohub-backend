package com.portfoliohub.backend.dto.project;

import java.time.LocalDate;

public record ProjectCreateRequestDto(
        String title,
        String description,
        String githubUrl,
        String liveDemoUrl,
        String imageUrl,
        LocalDate startDate,
        LocalDate endDate
) {}

