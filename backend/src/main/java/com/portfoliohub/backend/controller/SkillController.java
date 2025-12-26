package com.portfoliohub.backend.controller;
import com.portfoliohub.backend.Entities.Skill;
import com.portfoliohub.backend.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillRepository skillRepository;

    // 🌍 Public — list all skills
    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillRepository.findAll());
    }

    // 🌍 Public — skills by category (optional but very pro)
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Skill>> getByCategory(
            @PathVariable String category
    ) {
        return ResponseEntity.ok(
                skillRepository.findAll()
                        .stream()
                        .filter(skill -> category.equalsIgnoreCase(skill.getCategory()))
                        .toList()
        );
    }
}

