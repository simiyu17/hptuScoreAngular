package com.hptu.score.repository;

import com.hptu.score.entity.CountyAssessmentMetaData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@ApplicationScoped
public interface CountyAssessmentRepository extends JpaRepository<CountyAssessmentMetaData, Long> {

    List<CountyAssessmentMetaData> findByCountyCodeAndAssessmentQuarterAndAssessmentYear(String countyCode, String assessmentQuarter, String assessmentYear);

}
