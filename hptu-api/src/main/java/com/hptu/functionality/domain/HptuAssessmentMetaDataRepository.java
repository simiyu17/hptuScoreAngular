package com.hptu.functionality.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HptuAssessmentMetaDataRepository extends JpaRepository<HptuAssessmentMetaData, Long> {
}
