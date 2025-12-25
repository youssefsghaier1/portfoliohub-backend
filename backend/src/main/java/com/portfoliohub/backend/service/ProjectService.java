package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.Entities.Project;
import com.portfoliohub.backend.dto.project.ProjectCreateRequestDto;
import com.portfoliohub.backend.dto.project.ProjectResponseDto;
import com.portfoliohub.backend.dto.project.ProjectUpdateRequestDto;
import com.portfoliohub.backend.repository.ProfileRepository;
import com.portfoliohub.backend.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;

    // 🔐 Add project
    @Transactional
    public ProjectResponseDto addProject(Authentication auth, ProjectCreateRequestDto request) {

        Profile profile = profileRepository.findByUser_Email(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Project project = Project.builder()
                .profile(profile)
                .title(request.title())
                .description(request.description())
                .githubUrl(request.githubUrl())
                .liveDemoUrl(request.liveDemoUrl())
                .imageUrl(request.imageUrl())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .featured(false)
                .build();

        projectRepository.save(project);
        return mapToDto(project);
    }

    // 🔐 Update project
    @Transactional
    public ProjectResponseDto updateProject(
            Authentication auth,
            Long id,
            ProjectUpdateRequestDto request
    ) {
        Project project = projectRepository
                .findByIdAndProfile_User_Email(id, auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        project.setTitle(request.title());
        project.setDescription(request.description());
        project.setGithubUrl(request.githubUrl());
        project.setLiveDemoUrl(request.liveDemoUrl());
        project.setImageUrl(request.imageUrl());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        project.setFeatured(request.featured());

        return mapToDto(project);
    }

    // 🔐 Delete project
    @Transactional
    public void deleteProject(Authentication auth, Long id) {
        Project project = projectRepository
                .findByIdAndProfile_User_Email(id, auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        projectRepository.delete(project);
    }

    // 🌍 Public projects
    public List<ProjectResponseDto> getPublicProjects(String username) {
        return projectRepository
                .findByProfile_User_UsernameOrderByFeaturedDescCreatedAtDesc(username)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // 🔁 Mapper
    private ProjectResponseDto mapToDto(Project project) {
        return new ProjectResponseDto(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getGithubUrl(),
                project.getLiveDemoUrl(),
                project.getImageUrl(),
                project.getFeatured(),
                project.getStartDate(),
                project.getEndDate()
        );
    }
}
