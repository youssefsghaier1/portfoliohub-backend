package com.portfoliohub.backend.dto.message;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MessageCreateRequestDto(
        @NotBlank String senderName,
        @Email @NotBlank String senderEmail,
        @NotBlank String subject,
        @NotBlank String content
) {}
