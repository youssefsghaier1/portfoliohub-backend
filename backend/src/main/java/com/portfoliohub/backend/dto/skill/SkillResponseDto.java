package com.portfoliohub.backend.dto.skill;

public record SkillResponseDto(
        Long id,
        String name,
        String description,
        String category
) {}
