package com.portfoliohub.backend.repository;

import com.portfoliohub.backend.Entities.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    List<Certification> findByProfile_User_UsernameOrderByIssuedDateDesc(String username);
}
