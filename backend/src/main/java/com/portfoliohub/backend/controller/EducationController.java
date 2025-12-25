package com.portfoliohub.backend.controller;

import com.portfoliohub.backend.dto.education.EducationCreateRequestDto;
import com.portfoliohub.backend.dto.education.EducationResponseDto;
import com.portfoliohub.backend.dto.education.EducationUpdateRequestDto;
import com.portfoliohub.backend.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    // 🔐 Add education (current user)
    @PostMapping("/me")
    public ResponseEntity<EducationResponseDto> add(
            Authentication authentication,
            @RequestBody EducationCreateRequestDto request
    ) {
        return ResponseEntity.ok(
                educationService.addEducation(authentication, request)
        );
    }

    // 🔐 Update education (current user)
    @PutMapping("/me/{id}")
    public ResponseEntity<EducationResponseDto> update(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody EducationUpdateRequestDto request
    ) {
        return ResponseEntity.ok(
                educationService.updateEducation(authentication, id, request)
        );
    }

    // 🔐 Delete education (current user)
    @DeleteMapping("/me/{id}")
    public ResponseEntity<Void> delete(
            Authentication authentication,
            @PathVariable Long id
    ) {
        educationService.deleteEducation(authentication, id);
        return ResponseEntity.noContent().build();
    }

    // 🌍 Public education list by username
    @GetMapping("/{username}")
    public ResponseEntity<List<EducationResponseDto>> getPublic(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(
                educationService.getPublicEducations(username)
        );
    }
}
