package com.portfoliohub.backend.dto.education;

import java.time.LocalDate;

public record EducationCreateRequestDto(
        String institution,
        String degree,
        String fieldOfStudy,
        LocalDate startDate,
        LocalDate endDate,
        String location,
        String description
) {}
