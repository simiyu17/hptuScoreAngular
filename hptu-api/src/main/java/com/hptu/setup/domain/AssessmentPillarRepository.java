package com.hptu.setup.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentPillarRepository extends JpaRepository<AssessmentPillar, Long> {
}
