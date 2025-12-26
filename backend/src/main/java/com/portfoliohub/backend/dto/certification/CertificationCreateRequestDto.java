package com.portfoliohub.backend.dto.certification;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CertificationCreateRequestDto(

        @NotBlank
        String name,

        @NotBlank
        String organization,

        String credentialUrl,

        LocalDate issuedDate,

        LocalDate expirationDate,

        String description
) {}
