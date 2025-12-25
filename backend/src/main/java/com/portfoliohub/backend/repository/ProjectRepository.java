package com.portfoliohub.backend.repository;

import com.portfoliohub.backend.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // 🔐 Owner access
    Optional<Project> findByIdAndProfile_User_Email(Long id, String email);

    // 🌍 Public profile projects (featured first, newest first)
    List<Project> findByProfile_User_UsernameOrderByFeaturedDescCreatedAtDesc(String username);
}
