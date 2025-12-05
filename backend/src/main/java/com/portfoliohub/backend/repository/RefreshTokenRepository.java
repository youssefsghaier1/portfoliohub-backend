package com.portfoliohub.backend.repository;

import com.portfoliohub.backend.Entities.RefreshToken;
import com.portfoliohub.backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
