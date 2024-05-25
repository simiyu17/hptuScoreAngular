package com.hptu.score.service;

import java.util.List;
import java.util.Map;

import com.hptu.score.dto.CountyAssessmentDto;
import com.hptu.score.dto.CountyAssessmentResultDetailedDto;
import com.hptu.score.dto.CountySummaryDto;
import com.hptu.score.entity.CountyAssessment;
import com.hptu.score.entity.CountyAssessmentMetaData;
import com.hptu.score.entity.User;
import jakarta.validation.constraints.NotBlank;

public interface CountyAssessmentService {

	CountyAssessmentMetaData createCountyAssessment(CountyAssessmentDto countyAssessmentDto);
	
	CountyAssessmentMetaData addCountyAssessmentDetails(Long assessmentId, List<CountyAssessment> assessments);

	void clearCountyAssessments(Long assessmentId, User user);

	CountyAssessmentMetaData findCountyAssessmentMetaDataById(Long assessmentId);

	List<CountyAssessmentMetaData> getAvailableCountyAssessmentMetaDatas();

	CountyAssessmentMetaData getCountyAssessmentByCodeYearAndQuarter(String countyCode, String quarter, String year);

	void deleteCountyAssessmentMetaData(Long assessmentId);

	List<CountySummaryDto> getCountyAssessmentSummaryGroupedByCategory(Long assessmentId, String pillar);

	List<CountySummaryDto> getCountyAssessmentSummaryGroupedByPillar(Long assessmentId);

	CountyAssessmentResultDetailedDto getAssessmentSummary(String countyCode, String assessmentQuarter, String assessmentYear);

	CountyAssessmentResultDetailedDto getAssessmentPerformanceSummary(String assessmentYear, String countyCode, String assessmentQuarter, String pillar);
}
