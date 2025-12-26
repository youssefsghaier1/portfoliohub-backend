package com.portfoliohub.backend.dto.portfolio;

import com.portfoliohub.backend.dto.education.EducationResponseDto;
import com.portfoliohub.backend.dto.experience.ExperienceResponseDto;
import com.portfoliohub.backend.dto.project.ProjectResponseDto;
import com.portfoliohub.backend.dto.skill.SkillResponseDto;
import com.portfoliohub.backend.dto.profile.ProfileResponseDto;

import java.util.List;

public record PublicPortfolioResponseDto(
        String username,
        String fullName,
        String bio,
        String avatarUrl,
        String location,
        String website,

        List<ExperienceResponseDto> experiences,
        List<EducationResponseDto> educations,
        List<ProjectResponseDto> projects,
        List<SkillResponseDto> skills,

        int completionScore
) {}

