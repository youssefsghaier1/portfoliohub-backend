package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Education;
import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.dto.education.EducationCreateRequestDto;
import com.portfoliohub.backend.dto.education.EducationResponseDto;
import com.portfoliohub.backend.dto.education.EducationUpdateRequestDto;
import com.portfoliohub.backend.repository.EducationRepository;
import com.portfoliohub.backend.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final EducationRepository educationRepository;
    private final ProfileRepository profileRepository;

    // 🔐 ADD education
    @Transactional
    public EducationResponseDto addEducation(
            Authentication authentication,
            EducationCreateRequestDto request
    ) {
        Profile profile = profileRepository
                .findByUser_Email(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Education education = Education.builder()
                .profile(profile)
                .institution(request.institution())
                .degree(request.degree())
                .fieldOfStudy(request.fieldOfStudy())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .location(request.location())
                .description(request.description())
                .build();

        educationRepository.save(education);
        return mapToDto(education);
    }

    // 🔐 UPDATE education
    @Transactional
    public EducationResponseDto updateEducation(
            Authentication authentication,
            Long educationId,
            EducationUpdateRequestDto request
    ) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new EntityNotFoundException("Education not found"));

        if (!education.getProfile().getUser().getEmail()
                .equals(authentication.getName())) {
            throw new AccessDeniedException("Not allowed");
        }

        education.setInstitution(request.institution());
        education.setDegree(request.degree());
        education.setFieldOfStudy(request.fieldOfStudy());
        education.setStartDate(request.startDate());
        education.setEndDate(request.endDate());
        education.setLocation(request.location());
        education.setDescription(request.description());

        return mapToDto(education);
    }

    // 🔐 DELETE education
    @Transactional
    public void deleteEducation(
            Authentication authentication,
            Long educationId
    ) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new EntityNotFoundException("Education not found"));

        if (!education.getProfile().getUser().getEmail()
                .equals(authentication.getName())) {
            throw new AccessDeniedException("Not allowed");
        }

        educationRepository.delete(education);
    }

    // 🌍 PUBLIC education list
    public List<EducationResponseDto> getPublicEducations(String username) {

        Profile profile = profileRepository
                .findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        return educationRepository
                .findByProfile_IdOrderByStartDateDesc(profile.getId())
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // Mapper
    private EducationResponseDto mapToDto(Education e) {
        return new EducationResponseDto(
                e.getId(),
                e.getInstitution(),
                e.getDegree(),
                e.getFieldOfStudy(),
                e.getStartDate(),
                e.getEndDate(),
                e.getLocation(),
                e.getDescription()
        );
    }
}
