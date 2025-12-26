package com.portfoliohub.backend.controller;

import com.portfoliohub.backend.dto.message.MessageCreateRequestDto;
import com.portfoliohub.backend.dto.message.MessageResponseDto;
import com.portfoliohub.backend.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // 🌍 PUBLIC: send message
    @PostMapping("/{username}")
    public ResponseEntity<Void> send(
            @PathVariable String username,
            @Valid @RequestBody MessageCreateRequestDto request
    ) {
        messageService.sendMessage(username, request);
        return ResponseEntity.ok().build();
    }

    // 🔐 USER: inbox
    @GetMapping("/me")
    public ResponseEntity<List<MessageResponseDto>> inbox(Authentication auth) {
        return ResponseEntity.ok(messageService.getMyMessages(auth));
    }

    // 🔐 USER: mark read
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(
            @PathVariable Long id,
            Authentication auth
    ) {
        messageService.markAsRead(id, auth);
        return ResponseEntity.ok().build();
    }

    // 🔐 USER: delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication auth
    ) {
        messageService.deleteMessage(id, auth);
        return ResponseEntity.noContent().build();
    }
}
