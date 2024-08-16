package com.hptu.hptuassessment.service;



import com.hptu.authentication.domain.AppUser;
import com.hptu.hptuassessment.domain.CountyAssessment;
import com.hptu.hptuassessment.domain.CountyAssessmentMetaData;
import com.hptu.report.dto.CountyAssessmentDto;
import com.hptu.report.dto.CountyAssessmentResultDetailedDto;
import com.hptu.report.dto.CountySummaryDto;
import com.hptu.report.dto.HptuCountyAssessmentResultDetailedDto;

import java.util.List;

public interface CountyAssessmentServiceV2 {

	CountyAssessmentMetaData createCountyAssessment(CountyAssessmentDto countyAssessmentDto);
	
	CountyAssessmentMetaData addCountyAssessmentDetails(Long assessmentId, List<CountyAssessment> assessments);

	void clearCountyAssessments(Long assessmentId, AppUser user);

	CountyAssessmentMetaData findCountyAssessmentMetaDataById(Long assessmentId);

	List<CountyAssessmentMetaData> getAvailableCountyAssessmentMetaDatas();

	CountyAssessmentMetaData getCountyAssessmentByCodeYearAndQuarter(String countyCode, String quarter, String year);

	void deleteCountyAssessmentMetaData(Long assessmentId);

	List<CountySummaryDto> getCountyAssessmentSummaryGroupedByCategory(Long assessmentId, String pillar);

	List<CountySummaryDto> getCountyAssessmentSummaryGroupedByPillar(Long assessmentId);

	CountyAssessmentResultDetailedDto getAssessmentSummary(String countyCode, String assessmentQuarter, String assessmentYear);

	CountyAssessmentResultDetailedDto getAssessmentPerformanceSummary(String assessmentYear, String countyCode, String assessmentQuarter, String pillar);

	HptuCountyAssessmentResultDetailedDto getCountyHPTUAssessmentPerformanceSummary(String countyCode, String assessmentDate, String functionalityName);
}
