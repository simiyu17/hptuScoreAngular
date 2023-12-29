package com.hptu.score.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hptu.score.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

import com.hptu.score.entity.CountyAssessment;
import com.hptu.score.entity.CountyAssessmentStatus;
import com.hptu.score.repository.CountyAssessmentRepository;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CountyAssessmentServiceImpl implements CountyAssessmentService {
	
	private final CountyAssessmentRepository assessmentRepository;
	
	public CountyAssessmentServiceImpl(CountyAssessmentRepository assessmentRepository) {
		this.assessmentRepository = assessmentRepository;
	}

	@Transactional
	@Override
	public CountyAssessmentStatus saveCountyAssessment(CountyAssessmentStatus status) {
		return this.assessmentRepository.save(status);
	}

	@Override
	public CountyAssessmentStatus addCountyAssessmentDetails(Long assessmentId, List<CountyAssessment> assessments) {
		CountyAssessmentStatus status = this.findCountyAssessmentStatusById(assessmentId);
		status.addAssessments(assessments);
		return this.assessmentRepository.save(status);
	}

	@Transactional
	@Override
	public void clearCountyAssessments(Long assessmentId, User user) {
		CountyAssessmentStatus status = this.findCountyAssessmentStatusById(assessmentId);
		status.getAssessments().clear();
		status.setStatus(CountyAssessmentStatus.Status.INCOMPLETE);
		status.setLastModifiedBy(user);
	}

	@Override
	public CountyAssessmentStatus findCountyAssessmentStatusById(Long assessmentId) {
		return this.assessmentRepository.findById(assessmentId)
				.orElseThrow(() -> new IllegalArgumentException("No record with id"));
	}

	@Override
	public List<CountyAssessmentStatus> getAvailableCountyAssessmentStatuses() {
		return this.assessmentRepository.findAll();
	}

	@Override
	public CountyAssessmentStatus getCountyAssessmentByCodeYearAndQuarter(String countyCode, String quarter, String year) {
		List<CountyAssessmentStatus> assessmentStatuses = new ArrayList<>(); //this.assessmentRepository.findByCountyCodeAndAssessmentQuarterAndAssessmentYear(countyCode, quarter, year);
		return assessmentStatuses.isEmpty() ? null : assessmentStatuses.get(0);
	}

	@Override
	public void deleteCountyAssessmentStatus(Long assessmentId) {
		CountyAssessmentStatus status = this.findCountyAssessmentStatusById(assessmentId);
		this.assessmentRepository.delete(status);
	}

	@Override
	public List<Map<String, Object>> getAssessmentCountSummaryGroupedByCategory(Long assessmentId, String pillar) {
		return null; //this.assessmentRepository.getSummaryGroupedByCategory(assessmentId, pillar);
	}

	@Override
	public List<Map<String, Object>> getAssessmentSummaryGroupedByPillar(Long assessmentId) {
		return null; //this.assessmentRepository.getSummaryGroupedByPillar(assessmentId);
	}

}
