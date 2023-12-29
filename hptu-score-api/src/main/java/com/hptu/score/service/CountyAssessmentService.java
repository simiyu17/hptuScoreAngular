package com.hptu.score.service;

import java.util.List;
import java.util.Map;

import com.hptu.score.entity.CountyAssessment;
import com.hptu.score.entity.CountyAssessmentStatus;
import com.hptu.score.entity.User;
import org.springframework.data.repository.query.Param;

public interface CountyAssessmentService {

	CountyAssessmentStatus saveCountyAssessment(CountyAssessmentStatus status);
	
	CountyAssessmentStatus addCountyAssessmentDetails(Long assessmentId, List<CountyAssessment> assessments);

	void clearCountyAssessments(Long assessmentId, User user);

	CountyAssessmentStatus findCountyAssessmentStatusById(Long assessmentId);

	List<CountyAssessmentStatus> getAvailableCountyAssessmentStatuses();

	CountyAssessmentStatus getCountyAssessmentByCodeYearAndQuarter(String countyCode, String quarter,  String year);

	void deleteCountyAssessmentStatus(Long assessmentId);

	List<Map<String, Object>> getAssessmentCountSummaryGroupedByCategory(Long assessmentId, String pillar);

	List<Map<String, Object>> getAssessmentSummaryGroupedByPillar(Long assessmentId);
}
