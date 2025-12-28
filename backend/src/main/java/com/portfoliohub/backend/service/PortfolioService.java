package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.dto.education.EducationResponseDto;
import com.portfoliohub.backend.dto.experience.ExperienceResponseDto;
import com.portfoliohub.backend.dto.portfolio.PublicPortfolioResponseDto;
import com.portfoliohub.backend.dto.project.ProjectResponseDto;
import com.portfoliohub.backend.dto.skill.SkillResponseDto;
import com.portfoliohub.backend.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final ProfileRepository profileRepository;
    private final ProfileCompletionService profileCompletionService;

    public PublicPortfolioResponseDto getPublicPortfolio(String username) {

        Profile profile = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        int completionScore = profileCompletionService.calculateScore(profile);

        // 🔐 Public visibility rule
        if (!profile.getPublicProfile() || completionScore < 60) {
            throw new RuntimeException("Portfolio is not public");
        }

        List<ExperienceResponseDto> experiences = profile.getExperiences().stream()
                .sorted((a, b) -> b.getStartDate().compareTo(a.getStartDate()))
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

        List<EducationResponseDto> educations = profile.getEducations().stream()
                .sorted((a, b) -> b.getStartDate().compareTo(a.getStartDate()))
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

        List<ProjectResponseDto> projects = profile.getProjects().stream()
                .sorted((a, b) -> {
                    if (Boolean.TRUE.equals(a.getFeatured()) && !Boolean.TRUE.equals(b.getFeatured())) return -1;
                    if (!Boolean.TRUE.equals(a.getFeatured()) && Boolean.TRUE.equals(b.getFeatured())) return 1;
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

        List<SkillResponseDto> skills = profile.getSkills().stream()
                .map(skill -> new SkillResponseDto(
                        skill.getId(),
                        skill.getName(),
                        skill.getDescription(),
                        skill.getCategory()
                ))
                .toList();

        return new PublicPortfolioResponseDto(
                profile.getUser().getUsername(),
                profile.getFullName(),
                profile.getBio(),
                profile.getAvatarUrl(),
                profile.getLocation(),
                profile.getWebsite(),
                experiences,
                educations,
                projects,
                skills,
                completionScore
        );
    }
}
