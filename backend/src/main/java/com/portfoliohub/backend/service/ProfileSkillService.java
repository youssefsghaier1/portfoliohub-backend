package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.Entities.Skill;
import com.portfoliohub.backend.repository.ProfileRepository;
import com.portfoliohub.backend.repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfileSkillService {

    private final ProfileRepository profileRepository;
    private final SkillRepository skillRepository;

    @Transactional
    public Set<Skill> addSkill(Authentication auth, Long skillId) {

        Profile profile = profileRepository.findByUser_Email(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found"));

        profile.getSkills().add(skill);
        return profile.getSkills();
    }

    @Transactional
    public Set<Skill> removeSkill(Authentication auth, Long skillId) {

        Profile profile = profileRepository.findByUser_Email(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        profile.getSkills().removeIf(skill -> skill.getId().equals(skillId));
        return profile.getSkills();
    }
}
