package com.hptu.hptuassessment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountyAssessmentMetaDataRepository extends JpaRepository<CountyAssessmentMetaData, Long> {
    List<CountyAssessmentMetaData> findByCountyCodeAndAssessmentYearAndAssessmentQuarter(@Nullable String countyCode, @NonNull String assessmentYear, @Nullable String assessmentQuarter);


}
