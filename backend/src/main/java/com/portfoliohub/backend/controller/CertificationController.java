package com.portfoliohub.backend.controller;
import com.portfoliohub.backend.dto.certification.CertificationCreateRequestDto;
import com.portfoliohub.backend.dto.certification.CertificationResponseDto;
import com.portfoliohub.backend.service.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    // ======================
    // 🔐 ADD certification
    // ======================
    @PostMapping("/me")
    public ResponseEntity<CertificationResponseDto> add(
            Authentication auth,
            @Valid @RequestBody CertificationCreateRequestDto request
    ) {
        return ResponseEntity.ok(certificationService.add(auth, request));
    }

    // ======================
    // 🔐 UPDATE certification
    // ======================
    @PutMapping("/me/{id}")
    public ResponseEntity<CertificationResponseDto> update(
            Authentication auth,
            @PathVariable Long id,
            @Valid @RequestBody CertificationCreateRequestDto request
    ) throws AccessDeniedException {
        return ResponseEntity.ok(certificationService.update(auth, id, request));
    }

    // ======================
    // 🔐 DELETE certification
    // ======================
    @DeleteMapping("/me/{id}")
    public ResponseEntity<Void> delete(
            Authentication auth,
            @PathVariable Long id
    ) throws AccessDeniedException {
        certificationService.delete(auth, id);
        return ResponseEntity.noContent().build();
    }

    // ======================
    // 🌍 PUBLIC certifications
    // ======================
    @GetMapping("/{username}")
    public ResponseEntity<List<CertificationResponseDto>> getPublic(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(certificationService.getPublic(username));
    }
}
