package com.portfoliohub.backend.service;

import com.portfoliohub.backend.Entities.Profile;
import org.springframework.stereotype.Service;

@Service
public class ProfileCompletionService {

    public int calculateScore(Profile profile) {
        int score = 0;

        // ===== Profile basics (30) =====
        if (notBlank(profile.getFullName())) score += 5;
        if (notBlank(profile.getBio())) score += 5;
        if (notBlank(profile.getAvatarUrl())) score += 5;
        if (notBlank(profile.getLocation())) score += 5;
        if (notBlank(profile.getWebsite())) score += 5;
        if (profile.getBirthdate() != null) score += 5;

        // ===== Content sections (70) =====
        if (!profile.getExperiences().isEmpty()) score += 15;
        if (!profile.getEducations().isEmpty()) score += 15;
        if (!profile.getProjects().isEmpty()) score += 20;
        if (!profile.getSkills().isEmpty()) score += 10;
        if (!profile.getCertifications().isEmpty()) score += 10;

        return Math.min(score, 100);
    }

    public boolean isVerified(Profile profile) {
        return calculateScore(profile) == 100;
    }

    private boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
