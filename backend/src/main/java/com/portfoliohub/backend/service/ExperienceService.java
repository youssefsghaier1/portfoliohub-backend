package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Experience;
import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.dto.experience.ExperienceRequestDto;
import com.portfoliohub.backend.dto.experience.ExperienceResponseDto;
import com.portfoliohub.backend.repository.ExperienceRepository;
import com.portfoliohub.backend.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final ProfileRepository profileRepository;

    // 🔐 Get my experiences (sorted newest -> oldest)
    public List<ExperienceResponseDto> getMyExperiences(Authentication auth) {
        Profile profile = getMyProfile(auth);
        return experienceRepository
                .findByProfile_IdOrderByStartDateDescCreatedAtDesc(profile.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    // 🔐 Add experience
    @Transactional
    public ExperienceResponseDto addMyExperience(Authentication auth, ExperienceRequestDto req) {
        Profile profile = getMyProfile(auth);
        normalizeDates(req);

        Experience exp = Experience.builder()
                .profile(profile)
                .company(req.company())
                .position(req.position())
                .description(req.description())
                .location(req.location())
                .startDate(req.startDate())
                .endDate(req.isCurrent() != null && req.isCurrent() ? null : req.endDate())
                .isCurrent(req.isCurrent() != null && req.isCurrent())
                .build();

        return toDto(experienceRepository.save(exp));
    }

    // 🔐 Update experience (only if it belongs to me)
    @Transactional
    public ExperienceResponseDto updateMyExperience(Authentication auth, Long expId, ExperienceRequestDto req) {
        Profile profile = getMyProfile(auth);
        normalizeDates(req);

        Experience exp = experienceRepository.findById(expId)
                .orElseThrow(() -> new EntityNotFoundException("Experience not found"));

        if (!exp.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Forbidden: not your experience");
        }

        exp.setCompany(req.company());
        exp.setPosition(req.position());
        exp.setDescription(req.description());
        exp.setLocation(req.location());
        exp.setStartDate(req.startDate());
        exp.setIsCurrent(req.isCurrent() != null && req.isCurrent());
        exp.setEndDate(exp.getIsCurrent() ? null : req.endDate());

        return toDto(exp);
    }

    // 🔐 Delete experience (only if it belongs to me)
    @Transactional
    public void deleteMyExperience(Authentication auth, Long expId) {
        Profile profile = getMyProfile(auth);

        Experience exp = experienceRepository.findById(expId)
                .orElseThrow(() -> new EntityNotFoundException("Experience not found"));

        if (!exp.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Forbidden: not your experience");
        }

        experienceRepository.delete(exp);
    }

    // ===== Helpers =====

    private Profile getMyProfile(Authentication auth) {
        String email = auth.getName();
        return profileRepository.findByUser_Email(email)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

    private void normalizeDates(ExperienceRequestDto req) {
        if (req.endDate() != null && req.startDate().isAfter(req.endDate())) {
            throw new RuntimeException("Invalid dates: startDate must be <= endDate");
        }
    }

    private ExperienceResponseDto toDto(Experience exp) {
        return new ExperienceResponseDto(
                exp.getId(),
                exp.getCompany(),
                exp.getPosition(),
                exp.getDescription(),
                exp.getLocation(),
                exp.getStartDate(),
                exp.getEndDate(),
                exp.getIsCurrent()
        );
    }
}
