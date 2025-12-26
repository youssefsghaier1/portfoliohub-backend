package com.portfoliohub.backend.dto.certification;

import java.time.LocalDate;

public record CertificationResponseDto(
        Long id,
        String name,
        String organization,
        LocalDate issuedDate,
        LocalDate expirationDate,
        String credentialUrl
) {}
