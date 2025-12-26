package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.dto.education.EducationResponseDto;
import com.portfoliohub.backend.dto.experience.ExperienceResponseDto;
import com.portfoliohub.backend.dto.portfolio.PublicPortfolioResponseDto;
import com.portfoliohub.backend.dto.project.ProjectResponseDto;
import com.portfoliohub.backend.dto.skill.SkillResponseDto;
import com.portfoliohub.backend.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final ProfileRepository profileRepository;

    public PublicPortfolioResponseDto getPublicPortfolio(String username) {

        Profile profile = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        if (!profile.getIsPublic()) {
            throw new RuntimeException("Profile is private");
        }
        return new PublicPortfolioResponseDto(
                profile.getUser().getUsername(),
                profile.getFullName(),
                profile.getBio(),
                profile.getAvatarUrl(),
                profile.getLocation(),
                profile.getWebsite(),
                mapExperiences(profile),
                mapEducations(profile),
                mapProjects(profile),
                mapSkills(profile),
                calculateCompletionScore(profile)
        );
    }

    // ======================
    // 🔢 COMPLETION SCORE
    // ======================
    private int calculateCompletionScore(Profile profile) {
        int score = 0;
        // Profile basics (20)
        if (profile.getFullName() != null && !profile.getFullName().isBlank()) score += 5;
        if (profile.getBio() != null && !profile.getBio().isBlank()) score += 5;
        if (profile.getAvatarUrl() != null && !profile.getAvatarUrl().isBlank()) score += 5;
        if (profile.getLocation() != null && !profile.getLocation().isBlank()) score += 5;
        // Experiences (25)
        if (!profile.getExperiences().isEmpty()) score += 25;
        // Projects (25)
        if (!profile.getProjects().isEmpty()) score += 25;
        // Skills (20)
        if (!profile.getSkills().isEmpty()) score += 20;
        // Education (10)
        if (!profile.getEducations().isEmpty()) score += 10;
        if (score == 100) {
            profile.setVerified(true);
        }
        return Math.min(score, 100);
        //based on score the prfiles will be verified in frontend also when scores are at max they will be shown first
    }

    // ======================
    // 🔁 MAPPERS
    // ======================
    private List<ExperienceResponseDto> mapExperiences(Profile profile) {
        return profile.getExperiences().stream()
                .sorted(Comparator.comparing(e -> e.getStartDate(), Comparator.reverseOrder()))
                .map(exp -> new ExperienceResponseDto(
                        exp.getId(),
                        exp.getCompany(),
                        exp.getPosition(),
                        exp.getDescription(),
                        exp.getLocation(),
                        exp.getStartDate(),
                        exp.getEndDate(),
                        exp.getIsCurrent()
                ))
                .toList();
    }

    private List<EducationResponseDto> mapEducations(Profile profile) {
        return profile.getEducations().stream()
                .sorted(Comparator.comparing(e -> e.getStartDate(), Comparator.reverseOrder()))
                .map(edu -> new EducationResponseDto(
                        edu.getId(),
                        edu.getInstitution(),
                        edu.getDegree(),
                        edu.getFieldOfStudy(),
                        edu.getStartDate(),
                        edu.getEndDate(),
                        edu.getLocation(),
                        edu.getDescription()
                ))
                .toList();
    }

    private List<ProjectResponseDto> mapProjects(Profile profile) {
        return profile.getProjects().stream()
                .sorted((a, b) -> {
                    if (Boolean.TRUE.equals(a.getFeatured())) return -1;
                    if (Boolean.TRUE.equals(b.getFeatured())) return 1;
                    return b.getStartDate().compareTo(a.getStartDate());
                })
                .map(project -> new ProjectResponseDto(
                        project.getId(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getGithubUrl(),
                        project.getLiveDemoUrl(),
                        project.getImageUrl(),
                        project.getFeatured(),
                        project.getStartDate(),
                        project.getEndDate()
                ))
                .toList();
    }

    private List<SkillResponseDto> mapSkills(Profile profile) {
        return profile.getSkills().stream()
                .map(skill -> new SkillResponseDto(
                        skill.getId(),
                        skill.getName(),
                        skill.getDescription(),
                        skill.getCategory()
                ))
                .toList();
    }
}
