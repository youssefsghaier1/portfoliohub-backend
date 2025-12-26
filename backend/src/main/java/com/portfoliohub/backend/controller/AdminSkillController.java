package com.portfoliohub.backend.controller;

import com.portfoliohub.backend.Entities.Skill;
import com.portfoliohub.backend.service.AdminSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/skills")
@RequiredArgsConstructor
public class AdminSkillController {

    private final AdminSkillService adminSkillService;

    @GetMapping
    public ResponseEntity<List<Skill>> getAll() {
        return ResponseEntity.ok(adminSkillService.getAllSkills());
    }

    @PostMapping
    public ResponseEntity<Skill> create(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category
    ) {
        return ResponseEntity.ok(
                adminSkillService.createSkill(name, description, category)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skill> update(
            @PathVariable Long id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category
    ) {
        return ResponseEntity.ok(
                adminSkillService.updateSkill(id, description, category)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminSkillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
