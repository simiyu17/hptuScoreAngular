package com.hptu.score.service;

import java.util.List;
import java.util.Map;

import com.hptu.score.dto.CountyAssessmentDto;
import com.hptu.score.dto.CountySummaryDto;
import com.hptu.score.entity.CountyAssessment;
import com.hptu.score.entity.CountyAssessmentMetaData;
import com.hptu.score.entity.User;

public interface CountyAssessmentService {

	CountyAssessmentMetaData createCountyAssessment(CountyAssessmentDto countyAssessmentDto);
	
	CountyAssessmentMetaData addCountyAssessmentDetails(Long assessmentId, List<CountyAssessment> assessments);

	void clearCountyAssessments(Long assessmentId, User user);

	CountyAssessmentMetaData findCountyAssessmentMetaDataById(Long assessmentId);

	List<CountyAssessmentMetaData> getAvailableCountyAssessmentMetaDatas();

	CountyAssessmentMetaData getCountyAssessmentByCodeYearAndQuarter(String countyCode, String quarter, String year);

	void deleteCountyAssessmentMetaData(Long assessmentId);

	List<Map<String, Object>> getAssessmentCountSummaryGroupedByCategory(Long assessmentId, String pillar);

	List<Map<String, Object>> getAssessmentSummaryGroupedByPillar(Long assessmentId);

	List<CountySummaryDto> getCountyAssessmentSummary(Long assessmentId);
}
