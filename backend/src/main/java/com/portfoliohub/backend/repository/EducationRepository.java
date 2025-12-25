package com.portfoliohub.backend.repository;

import com.portfoliohub.backend.Entities.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {

    List<Education> findByProfile_IdOrderByStartDateDesc(Long profileId);
}
