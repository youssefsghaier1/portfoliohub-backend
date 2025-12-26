package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.dto.skill.SkillAssignRequestDto;
import com.portfoliohub.backend.dto.skill.SkillResponseDto;
import com.portfoliohub.backend.repository.ProfileRepository;
import com.portfoliohub.backend.repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public void addSkillToProfile(Authentication auth, SkillAssignRequestDto request) {
        Profile profile = profileRepository
                .findByUser_Email(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        if (!skillRepository.existsById(request.skillId())) {
            throw new EntityNotFoundException("Skill not found");
        }

        profileRepository.addSkillToProfile(
                profile.getId(),
                request.skillId(),
                request.level().name()
        );
    }

    public List<SkillResponseDto> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(skill -> new SkillResponseDto(
                        skill.getId(),
                        skill.getName(),
                        skill.getDescription(),
                        skill.getCategory()
                ))
                .toList();
    }
}
