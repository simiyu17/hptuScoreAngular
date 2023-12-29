package com.hptu.score.repository;

import com.hptu.score.entity.AssessmentPillar;
import jakarta.enterprise.context.ApplicationScoped;
import org.springframework.data.jpa.repository.JpaRepository;

@ApplicationScoped
public interface AssessmentPillarRepository extends JpaRepository<AssessmentPillar, Long> {
}
