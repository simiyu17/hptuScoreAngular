package com.hptu.score.service;

import com.hptu.score.dto.CountyAssessmentDto;
import com.hptu.score.dto.CountySummaryDto;
import com.hptu.score.entity.CountyAssessment;
import com.hptu.score.entity.CountyAssessmentMetaData;
import com.hptu.score.entity.User;
import com.hptu.score.repository.CountyAssessmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CountyAssessmentServiceImpl implements CountyAssessmentService {
	
	private final CountyAssessmentRepository assessmentRepository;
	@Inject
	EntityManager entityManager;
	
	public CountyAssessmentServiceImpl(CountyAssessmentRepository assessmentRepository) {
		this.assessmentRepository = assessmentRepository;
	}

	@Transactional
	@Override
	public CountyAssessmentMetaData createCountyAssessment(CountyAssessmentDto countyAssessmentDto){
		CountyAssessmentMetaData metaData = CountyAssessmentMetaData.createCountyAssessmentMetaData(countyAssessmentDto.getAssessmentMetaDataDto());
		List<CountyAssessment> assessments = countyAssessmentDto.getAssessments().stream()
				.map(a -> new CountyAssessment(a.getPillarName(), a.getCategory(), a.getChoiceText(), a.getChoiceScore(), a.getMaxScore(), a.getScoreRemarks())).toList();
		metaData.addAssessments(assessments);
		return this.assessmentRepository.save(metaData);
	}

	@Override
	public CountyAssessmentMetaData addCountyAssessmentDetails(Long assessmentId, List<CountyAssessment> assessments) {
		CountyAssessmentMetaData status = this.findCountyAssessmentMetaDataById(assessmentId);
		status.addAssessments(assessments);
		return this.assessmentRepository.save(status);
	}

	@Transactional
	@Override
	public void clearCountyAssessments(Long assessmentId, User user) {
		CountyAssessmentMetaData status = this.findCountyAssessmentMetaDataById(assessmentId);
		status.getAssessments().clear();
		status.setStatus(CountyAssessmentMetaData.Status.INCOMPLETE);
		status.setLastModifiedBy(user);
	}

	@Override
	public CountyAssessmentMetaData findCountyAssessmentMetaDataById(Long assessmentId) {
		return this.assessmentRepository.findById(assessmentId)
				.orElseThrow(() -> new IllegalArgumentException("No record with id"));
	}

	@Override
	public List<CountyAssessmentMetaData> getAvailableCountyAssessmentMetaDatas() {
		return this.assessmentRepository.findAll();
	}

	@Override
	public CountyAssessmentMetaData getCountyAssessmentByCodeYearAndQuarter(String countyCode, String quarter, String year) {
		List<CountyAssessmentMetaData> assessmentStatuses = this.assessmentRepository.findByCountyCodeAndAssessmentQuarterAndAssessmentYear(countyCode, quarter, year);
		return assessmentStatuses.isEmpty() ? null : assessmentStatuses.get(0);
	}

	@Override
	public void deleteCountyAssessmentMetaData(Long assessmentId) {
		CountyAssessmentMetaData status = this.findCountyAssessmentMetaDataById(assessmentId);
		this.assessmentRepository.delete(status);
	}

	@Override
	public List<CountySummaryDto> getCountyAssessmentSummaryGroupedByCategory(Long assessmentId, String pillar) {
		List<CountySummaryDto> result = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);
		Root<CountyAssessment> assessmentRoot = query.from(CountyAssessment.class);
		query.groupBy(assessmentRoot.get(CountyAssessment.CATEGORY_FIELD));
		query.multiselect(assessmentRoot.get(CountyAssessment.CATEGORY_FIELD), criteriaBuilder.sum(assessmentRoot.get(CountyAssessment.MAX_SCORE_FIELD)), criteriaBuilder.sum(assessmentRoot.get(CountyAssessment.CHOICE_SCORE_FIELD)));
		Predicate[] predicates = new Predicate[2];
		predicates[0] =  criteriaBuilder.equal(assessmentRoot.get(CountyAssessment.META_DATA_ID_FIELD).get("id"), assessmentId);
		predicates[1] =  criteriaBuilder.equal(assessmentRoot.get(CountyAssessment.PILLAR_FIELD), pillar);
		TypedQuery<Tuple> typedQuery = entityManager.createQuery(query.where(predicates));
		typedQuery.getResultList().forEach(t -> result.add(new CountySummaryDto(new CountySummaryDto.Summary(assessmentId, "", t.get(0, String.class), t.get(1, Integer.class), t.get(2, Integer.class)))));
		return  result;
	}

	@Override
	public List<CountySummaryDto> getCountyAssessmentSummaryGroupedByPillar(Long assessmentId) {
		List<CountySummaryDto> result = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);
		Root<CountyAssessment> assessmentRoot = query.from(CountyAssessment.class);
		query.groupBy(assessmentRoot.get(CountyAssessment.PILLAR_FIELD));
		query.multiselect(assessmentRoot.get(CountyAssessment.PILLAR_FIELD), criteriaBuilder.sum(assessmentRoot.get(CountyAssessment.MAX_SCORE_FIELD)), criteriaBuilder.sum(assessmentRoot.get(CountyAssessment.CHOICE_SCORE_FIELD)));
		Predicate[] predicates = new Predicate[1];
		predicates[0] =  criteriaBuilder.equal(assessmentRoot.get(CountyAssessment.META_DATA_ID_FIELD).get("id"), assessmentId);
		TypedQuery<Tuple> typedQuery = entityManager.createQuery(query.where(predicates));
		typedQuery.getResultList().forEach(t -> result.add(new CountySummaryDto(new CountySummaryDto.Summary(assessmentId, t.get(0, String.class), "", t.get(1, Integer.class), t.get(2, Integer.class)))));
		return  result;
	}

}
