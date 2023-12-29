package com.hptu.score.repository;

import com.hptu.score.entity.CountyAssessmentStatus;
import jakarta.enterprise.context.ApplicationScoped;
import org.springframework.data.jpa.repository.JpaRepository;


@ApplicationScoped
public interface CountyAssessmentRepository extends JpaRepository<CountyAssessmentStatus, Long> {

}
