package com.portfoliohub.backend.controller;

import com.portfoliohub.backend.dto.experience.ExperienceRequestDto;
import com.portfoliohub.backend.dto.experience.ExperienceResponseDto;
import com.portfoliohub.backend.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/experience")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    // 🔐 Get my experiences
    @GetMapping("/me")
    public ResponseEntity<List<ExperienceResponseDto>> getMyExperiences(Authentication auth) {
        return ResponseEntity.ok(experienceService.getMyExperiences(auth));
    }

    // 🔐 Add experience
    @PostMapping("/me")
    public ResponseEntity<ExperienceResponseDto> addMyExperience(
            Authentication auth,
            @Valid @RequestBody ExperienceRequestDto req
    ) {
        return ResponseEntity.ok(experienceService.addMyExperience(auth, req));
    }

    // 🔐 Update experience
    @PutMapping("/me/{id}")
    public ResponseEntity<ExperienceResponseDto> updateMyExperience(
            Authentication auth,
            @PathVariable Long id,
            @Valid @RequestBody ExperienceRequestDto req
    ) {
        return ResponseEntity.ok(experienceService.updateMyExperience(auth, id, req));
    }

    // 🔐 Delete experience
    @DeleteMapping("/me/{id}")
    public ResponseEntity<Void> deleteMyExperience(Authentication auth, @PathVariable Long id) {
        experienceService.deleteMyExperience(auth, id);
        return ResponseEntity.noContent().build();
    }
}
