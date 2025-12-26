package com.portfoliohub.backend.dto.skill;

import com.portfoliohub.backend.Enums.SkillLevel;
import jakarta.validation.constraints.NotNull;
public record SkillAssignRequestDto(
        @NotNull
        Long skillId,

        @NotNull
        SkillLevel level
) {}

