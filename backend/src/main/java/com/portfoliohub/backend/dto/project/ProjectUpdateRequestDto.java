package com.portfoliohub.backend.dto.project;

import java.time.LocalDate;

public record ProjectUpdateRequestDto(
        String title,
        String description,
        String githubUrl,
        String liveDemoUrl,
        String imageUrl,
        LocalDate startDate,
        LocalDate endDate,
        Boolean featured
) {}

