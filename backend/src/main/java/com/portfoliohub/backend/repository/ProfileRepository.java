package com.portfoliohub.backend.repository;

import com.portfoliohub.backend.Entities.Profile;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(Long userId);
    Optional<Profile> findByUser_Email(String email);
    Optional<Profile> findByUser_Username(String username);

    @Modifying
    @Query(
            value = """
    INSERT INTO profile_skills(profile_id, skill_id, skill_level)
    VALUES (:profileId, :skillId, :level)
    """,
            nativeQuery = true
    )
    void addSkillToProfile(
            Long profileId,
            Long skillId,
            String level
    );

}
