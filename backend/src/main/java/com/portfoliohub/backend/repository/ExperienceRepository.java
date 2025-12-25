package com.portfoliohub.backend.repository;

import com.portfoliohub.backend.Entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    // newest -> oldest
    List<Experience> findByProfile_IdOrderByStartDateDescCreatedAtDesc(Long profileId);
}
