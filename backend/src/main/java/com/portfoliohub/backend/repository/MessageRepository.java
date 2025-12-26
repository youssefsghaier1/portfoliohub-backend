package com.portfoliohub.backend.repository;

import com.portfoliohub.backend.Entities.Message;
import com.portfoliohub.backend.Entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByReceiverProfileOrderByCreatedAtDesc(Profile profile);
}
