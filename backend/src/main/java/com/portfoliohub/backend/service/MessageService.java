package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Message;
import com.portfoliohub.backend.Entities.Profile;
import com.portfoliohub.backend.dto.message.MessageCreateRequestDto;
import com.portfoliohub.backend.dto.message.MessageResponseDto;
import com.portfoliohub.backend.repository.MessageRepository;
import com.portfoliohub.backend.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ProfileRepository profileRepository;

    // 🌍 PUBLIC: send message to portfolio owner
    @Transactional
    public void sendMessage(String username, MessageCreateRequestDto request) {

        Profile profile = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Message message = Message.builder()
                .senderName(request.senderName())
                .senderEmail(request.senderEmail())
                .subject(request.subject())
                .content(request.content())
                .receiverProfile(profile)
                .isRead(false)
                .build();

        messageRepository.save(message);
    }

    // 🔐 USER: read my messages
    public List<MessageResponseDto> getMyMessages(Authentication auth) {

        Profile profile = profileRepository.findByUser_Email(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        return messageRepository.findByReceiverProfileOrderByCreatedAtDesc(profile)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // 🔐 USER: mark as read
    @Transactional
    public void markAsRead(Long id, Authentication auth) {

        Message message = getOwnedMessage(id, auth);
        message.setIsRead(true);
    }

    // 🔐 USER: delete message
    @Transactional
    public void deleteMessage(Long id, Authentication auth) {

        Message message = getOwnedMessage(id, auth);
        messageRepository.delete(message);
    }

    private Message getOwnedMessage(Long id, Authentication auth) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));

        String email = auth.getName();
        if (!message.getReceiverProfile().getUser().getEmail().equals(email)) {
            throw new SecurityException("Access denied");
        }

        return message;
    }

    private MessageResponseDto mapToDto(Message m) {
        return new MessageResponseDto(
                m.getId(),
                m.getSenderName(),
                m.getSenderEmail(),
                m.getSubject(),
                m.getContent(),
                m.getIsRead(),
                m.getCreatedAt()
        );
    }
}
