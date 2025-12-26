package com.portfoliohub.backend.controller;

import com.portfoliohub.backend.dto.certification.CertificationCreateRequestDto;
import com.portfoliohub.backend.dto.certification.CertificationResponseDto;
import com.portfoliohub.backend.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    // 🔐 Add certification
    @PostMapping("/me")
    public ResponseEntity<CertificationResponseDto> add(
            Authentication auth,
            @RequestBody CertificationCreateRequestDto request
    ) {
        return ResponseEntity.ok(certificationService.add(auth, request));
    }

    // 🌍 Public
    @GetMapping("/{username}")
    public ResponseEntity<List<CertificationResponseDto>> getPublic(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(certificationService.getPublic(username));
    }
}
