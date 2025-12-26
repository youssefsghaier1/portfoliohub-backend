package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Certification;
import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.dto.certification.CertificationCreateRequestDto;
import com.portfoliohub.backend.dto.certification.CertificationResponseDto;
import com.portfoliohub.backend.repository.CertificationRepository;
import com.portfoliohub.backend.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final ProfileRepository profileRepository;

    // 🔐 ADD
    @Transactional
    public CertificationResponseDto add(
            Authentication auth,
            CertificationCreateRequestDto request
    ) {
        Profile profile = getProfile(auth);

        Certification certification = Certification.builder()
                .profile(profile)
                .name(request.name())
                .organization(request.organization())
                .issuedDate(request.issuedDate())
                .expirationDate(request.expirationDate())
                .credentialUrl(request.credentialUrl())
                .createdAt(LocalDateTime.now())
                .build();

        certificationRepository.save(certification);
        return mapToDto(certification);
    }

    // 🔐 UPDATE
    @Transactional
    public CertificationResponseDto update(
            Authentication auth,
            Long id,
            CertificationCreateRequestDto request
    ) throws AccessDeniedException {

        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certification not found"));

        Profile profile = getProfile(auth);

        if (!certification.getProfile().getId().equals(profile.getId())) {
            throw new AccessDeniedException("You cannot update this certification");
        }

        certification.setName(request.name());
        certification.setOrganization(request.organization());
        certification.setIssuedDate(request.issuedDate());
        certification.setExpirationDate(request.expirationDate());
        certification.setCredentialUrl(request.credentialUrl());

        return mapToDto(certification);
    }

    // 🔐 DELETE
    @Transactional
    public void delete(Authentication auth, Long id) throws AccessDeniedException {

        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certification not found"));

        Profile profile = getProfile(auth);

        if (!certification.getProfile().getId().equals(profile.getId())) {
            throw new AccessDeniedException("You cannot delete this certification");
        }

        certificationRepository.delete(certification);
    }

    // 🌍 PUBLIC
    public List<CertificationResponseDto> getPublic(String username) {

        return certificationRepository
                .findByProfile_User_UsernameOrderByIssuedDateDesc(username)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // ======================
    // Helpers
    // ======================

    private Profile getProfile(Authentication auth) {
        return profileRepository.findByUser_Email(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

    private CertificationResponseDto mapToDto(Certification cert) {
        return new CertificationResponseDto(
                cert.getId(),
                cert.getName(),
                cert.getOrganization(),
                cert.getIssuedDate(),
                cert.getExpirationDate(),
                cert.getCredentialUrl()
        );
    }
}
