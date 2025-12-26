package com.portfoliohub.backend.dto.message;

import java.time.LocalDateTime;

public record MessageResponseDto(
        Long id,
        String senderName,
        String senderEmail,
        String subject,
        String content,
        Boolean isRead,
        LocalDateTime createdAt
) {}
