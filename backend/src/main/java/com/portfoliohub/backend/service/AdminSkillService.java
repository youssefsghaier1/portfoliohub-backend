package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Skill;
import com.portfoliohub.backend.repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSkillService {

    private final SkillRepository skillRepository;

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Transactional
    public Skill createSkill(String name, String description, String category) {

        if (skillRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new IllegalArgumentException("Skill already exists");
        }

        Skill skill = Skill.builder()
                .name(name)
                .description(description)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        return skillRepository.save(skill);
    }

    @Transactional
    public Skill updateSkill(Long id, String description, String category) {

        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found"));

        skill.setDescription(description);
        skill.setCategory(category);

        return skill;
    }

    @Transactional
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }
}
