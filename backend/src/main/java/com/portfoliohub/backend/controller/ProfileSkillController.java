package com.portfoliohub.backend.controller;
import com.portfoliohub.backend.Entities.Skill;
import com.portfoliohub.backend.service.ProfileSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/profile/me/skills")
@RequiredArgsConstructor
public class ProfileSkillController {

    private final ProfileSkillService profileSkillService;

    @PostMapping("/{skillId}")
    public ResponseEntity<Set<Skill>> addSkill(
            Authentication auth,
            @PathVariable Long skillId
    ) {
        return ResponseEntity.ok(
                profileSkillService.addSkill(auth, skillId)
        );
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Set<Skill>> removeSkill(
            Authentication auth,
            @PathVariable Long skillId
    ) {
        return ResponseEntity.ok(
                profileSkillService.removeSkill(auth, skillId)
        );
    }
}
