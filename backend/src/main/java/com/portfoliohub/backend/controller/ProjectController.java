package com.portfoliohub.backend.controller;

import com.portfoliohub.backend.dto.project.ProjectCreateRequestDto;
import com.portfoliohub.backend.dto.project.ProjectResponseDto;
import com.portfoliohub.backend.dto.project.ProjectUpdateRequestDto;
import com.portfoliohub.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // 🔐 Add
    @PostMapping("/me")
    public ResponseEntity<ProjectResponseDto> add(
            Authentication auth,
            @RequestBody ProjectCreateRequestDto request
    ) {
        return ResponseEntity.ok(projectService.addProject(auth, request));
    }

    // 🔐 Update
    @PutMapping("/me/{id}")
    public ResponseEntity<ProjectResponseDto> update(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody ProjectUpdateRequestDto request
    ) {
        return ResponseEntity.ok(projectService.updateProject(auth, id, request));
    }

    // 🔐 Delete
    @DeleteMapping("/me/{id}")
    public ResponseEntity<Void> delete(
            Authentication auth,
            @PathVariable Long id
    ) {
        projectService.deleteProject(auth, id);
        return ResponseEntity.noContent().build();
    }

    // 🌍 Public projects
    @GetMapping("/{username}")
    public ResponseEntity<List<ProjectResponseDto>> getPublic(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(projectService.getPublicProjects(username));
    }
}
