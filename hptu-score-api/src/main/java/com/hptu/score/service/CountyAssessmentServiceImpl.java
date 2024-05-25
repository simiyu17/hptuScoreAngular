package com.hptu.score.service;

import com.hptu.score.dto.CountyAssessmentDto;
import com.hptu.score.dto.CountyAssessmentResultDetailedDto;
import com.hptu.score.dto.CountySummaryDto;
import com.hptu.score.dto.ReportByPillar;
import com.hptu.score.entity.BaseEntity;
import com.hptu.score.entity.CountyAssessment;
import com.hptu.score.entity.CountyAssessmentMetaData;
import com.hptu.score.entity.User;
import com.hptu.score.exception.CountyAssessmentException;
import com.hptu.score.repository.CountyAssessmentRepository;
import com.hptu.score.util.CommonUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.hptu.score.entity.CountyAssessment.COUNTY_CODE_FIELD;

@ApplicationScoped
public class CountyAssessmentServiceImpl implements CountyAssessmentService {
	
	private final CountyAssessmentRepository assessmentRepository;

	private final EntityManager entityManager;
	
	public CountyAssessmentServiceImpl(CountyAssessmentRepository assessmentRepository, EntityManager entityManager) {
		this.assessmentRepository = assessmentRepository;
        this.entityManager = entityManager;
    }

	@Transactional
	@Override
	public CountyAssessmentMetaData createCountyAssessment(CountyAssessmentDto countyAssessmentDto){
		try{
		CountyAssessmentMetaData metaData = CountyAssessmentMetaData.createCountyAssessmentMetaData(countyAssessmentDto.getAssessmentMetaDataDto());
		List<CountyAssessment> assessments = countyAssessmentDto.getAssessments().stream()
				.map(a -> new CountyAssessment(a.getPillarName(), a.getCategory(), a.getChoiceText(), a.getChoiceScore(), a.getMaxScore(), a.getScoreRemarks())).toList();
		areAssessmentsValid(assessments);
		metaData.addAssessments(assessments);
		return this.assessmentRepository.save(metaData);
	}catch (Exception e){
		throw new CountyAssessmentException("Error: "+e.getMessage());
	}
	}

	private void areAssessmentsValid(List<CountyAssessment> assessments){
		if(assessments.isEmpty() || assessments.stream().anyMatch(as -> as.getChoiceScore() < 1)){
			throw new CountyAssessmentException("Please make assessment for each category and try again");
		}
	}

	@Override
	public CountyAssessmentMetaData addCountyAssessmentDetails(Long assessmentId, List<CountyAssessment> assessments) {
		try{
		CountyAssessmentMetaData status = this.findCountyAssessmentMetaDataById(assessmentId);
		status.addAssessments(assessments);
		return this.assessmentRepository.save(status);
	}catch (Exception e){
		throw new CountyAssessmentException("Error: "+e.getMessage());
	}
	}

	@Transactional
	@Override
	public void clearCountyAssessments(Long assessmentId, User user) {
		try{
		CountyAssessmentMetaData status = this.findCountyAssessmentMetaDataById(assessmentId);
		status.getAssessments().clear();
		status.setStatus(CountyAssessmentMetaData.Status.INCOMPLETE);
		status.setLastModifiedBy(user);
	}catch (Exception e){
		throw new CountyAssessmentException("Error: "+e.getMessage());
	}
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
		try{
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
		typedQuery.getResultList().forEach(t -> result.add(new CountySummaryDto(new CountySummaryDto.Summary("", t.get(0, String.class), t.get(1, Integer.class), t.get(2, Integer.class), 1))));
		return  result;
	}catch (Exception e){
		throw new CountyAssessmentException("Error: "+e.getMessage());
	}
	}

	@Override
	public List<CountySummaryDto> getCountyAssessmentSummaryGroupedByPillar(Long assessmentId) {
		try{
		List<CountySummaryDto> result = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);
		Root<CountyAssessment> assessmentRoot = query.from(CountyAssessment.class);
		query.groupBy(assessmentRoot.get(CountyAssessment.PILLAR_FIELD));
		query.multiselect(assessmentRoot.get(CountyAssessment.PILLAR_FIELD), criteriaBuilder.sum(assessmentRoot.get(CountyAssessment.MAX_SCORE_FIELD)), criteriaBuilder.sum(assessmentRoot.get(CountyAssessment.CHOICE_SCORE_FIELD)));
		Predicate[] predicates = new Predicate[1];
		predicates[0] =  criteriaBuilder.equal(assessmentRoot.get(CountyAssessment.META_DATA_ID_FIELD).get("id"), assessmentId);
		TypedQuery<Tuple> typedQuery = entityManager.createQuery(query.where(predicates));
		typedQuery.getResultList().forEach(t -> result.add(new CountySummaryDto(new CountySummaryDto.Summary(t.get(0, String.class), "", t.get(1, Integer.class), t.get(2, Integer.class), 1))));
		return  result;
	}catch (Exception e){
		throw new CountyAssessmentException("Error: "+e.getMessage());
	}
	}

	@Override
	public CountyAssessmentResultDetailedDto getAssessmentSummary(String countyCode, String assessmentQuarter, String assessmentYear) {
		try{
		if (StringUtils.isBlank(assessmentYear)){
			assessmentYear = String.valueOf(LocalDate.now(ZoneId.systemDefault()).getYear());
		}
		CountyAssessmentMetaData metaData = this.getAvailableCountyAssessmentMetaDatas().stream().max(Comparator.comparing(BaseEntity::getDateCreated)).orElse(null);
		if (StringUtils.isNoneBlank(countyCode) && StringUtils.isNoneBlank(assessmentQuarter) && StringUtils.isNoneBlank(assessmentYear)){
			metaData = this.getCountyAssessmentByCodeYearAndQuarter(countyCode, assessmentQuarter, assessmentYear);
		}
		List<CountySummaryDto> countySummaryDtos = new ArrayList<>();
		List<ReportByPillar.PillarDataPointModel> summaryDataPoints = new ArrayList<>();
		if (Objects.nonNull(metaData)){
			countySummaryDtos = this.getCountyAssessmentSummaryGroupedByPillar(metaData.getId());
			summaryDataPoints = countySummaryDtos.stream()
					.map(s -> new ReportByPillar.PillarDataPointModel(s.getPillarName(), s.getScorePercent())).toList();
		}
		return new CountyAssessmentResultDetailedDto(countySummaryDtos, summaryDataPoints, "");
	}catch (Exception e){
		throw new CountyAssessmentException("Error: "+e.getMessage());
	}
	}

	@Override
	public CountyAssessmentResultDetailedDto getAssessmentPerformanceSummary(String assessmentYear, String countyCode, String assessmentQuarter, String pillar) {
		try {
			if (StringUtils.isBlank(assessmentYear)){
				assessmentYear = String.valueOf(LocalDate.now(ZoneId.systemDefault()).getYear());
			}
			var groupByPillar = StringUtils.isBlank(pillar);
			List<CountySummaryDto> countySummaryDtos = new ArrayList<>();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);
			Root<CountyAssessment> assessmentRoot = query.from(CountyAssessment.class);
			if (groupByPillar){
				query.groupBy(assessmentRoot.get(CountyAssessment.PILLAR_FIELD));
				query.multiselect(assessmentRoot.get(CountyAssessment.PILLAR_FIELD), criteriaBuilder.sum(assessmentRoot.get(CountyAssessment.MAX_SCORE_FIELD)), criteriaBuilder.sum(assessmentRoot.get(CountyAssessment.CHOICE_SCORE_FIELD)));
			}else {
				query.groupBy(assessmentRoot.get(CountyAssessment.CATEGORY_FIELD));
				query.multiselect(assessmentRoot.get(CountyAssessment.CATEGORY_FIELD), criteriaBuilder.sum(assessmentRoot.get(CountyAssessment.MAX_SCORE_FIELD)), criteriaBuilder.sum(assessmentRoot.get(CountyAssessment.CHOICE_SCORE_FIELD)));
			}
			var summaryTitle = String.format("Assessment Summary For Year %s", assessmentYear);
			var countyNumber = 1;
			List<Predicate> predicateList = new ArrayList<>();
			predicateList.add(criteriaBuilder.equal(assessmentRoot.get(CountyAssessment.META_DATA_ID_FIELD).get("assessmentYear"), assessmentYear));
			if (StringUtils.isNotBlank(assessmentQuarter)){
				predicateList.add(criteriaBuilder.equal(assessmentRoot.get(CountyAssessment.META_DATA_ID_FIELD).get("assessmentQuarter"), assessmentQuarter));
				summaryTitle = String.format("Assessment Summary For %s County,  Year %s and Quarter %s", CommonUtil.getCountyByCode(countyCode), assessmentYear, assessmentQuarter);
				countyNumber = getDistinctCountiesCount(assessmentYear, assessmentQuarter);
			}else {
				countyNumber = getDistinctCountiesCount(assessmentYear, null);
			}
			if (StringUtils.isNotBlank(pillar)){
				predicateList.add(criteriaBuilder.equal(assessmentRoot.get(CountyAssessment.PILLAR_FIELD), pillar));
			}
			if (StringUtils.isNotBlank(countyCode)){
				predicateList.add(criteriaBuilder.equal(assessmentRoot.get(CountyAssessment.META_DATA_ID_FIELD).get(COUNTY_CODE_FIELD), countyCode));
				summaryTitle = String.format("Assessment Summary For %s County,  Year %s", CommonUtil.getCountyByCode(countyCode), assessmentYear);
				countyNumber = 1;
			}

			TypedQuery<Tuple> typedQuery = entityManager.createQuery(query.where(predicateList.toArray(new Predicate[0])));
			List<ReportByPillar.PillarDataPointModel> summaryDataPoints;
			final var countyCount = countyNumber;
			if (groupByPillar){
				typedQuery.getResultList().forEach(t -> countySummaryDtos.add(new CountySummaryDto(new CountySummaryDto.Summary(t.get(0, String.class), "", t.get(1, Integer.class), t.get(2, Integer.class), countyCount))));
				summaryDataPoints = countySummaryDtos.stream()
						.map(s -> new ReportByPillar.PillarDataPointModel(s.getPillarName(), s.getScorePercent())).toList();
			}else {
				typedQuery.getResultList().forEach(t -> countySummaryDtos.add(new CountySummaryDto(new CountySummaryDto.Summary("", t.get(0, String.class), t.get(1, Integer.class), t.get(2, Integer.class), countyCount))));
				summaryDataPoints = countySummaryDtos.stream()
						.map(s -> new ReportByPillar.PillarDataPointModel(s.getCategory(), BigDecimal.valueOf(s.getChoiceScore()))).toList();
			}
			return  new CountyAssessmentResultDetailedDto(countySummaryDtos, summaryDataPoints, summaryTitle);
		}catch (Exception e){
			throw new CountyAssessmentException("Error: "+e.getMessage());
		}
	}

	private int getDistinctCountiesCount(String assessmentYear, String assessmentQuarter){
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);
			Root<CountyAssessmentMetaData> assessmentRoot = query.from(CountyAssessmentMetaData.class);
			query.groupBy(assessmentRoot.get(COUNTY_CODE_FIELD));
			query.multiselect(assessmentRoot.get(COUNTY_CODE_FIELD), criteriaBuilder.count(assessmentRoot.get("id")));
			List<Predicate> predicateList = new ArrayList<>();
			predicateList.add(criteriaBuilder.equal(assessmentRoot.get("assessmentYear"), assessmentYear));
			if (StringUtils.isNotBlank(assessmentQuarter)){
				predicateList.add(criteriaBuilder.equal(assessmentRoot.get("assessmentQuarter"), assessmentQuarter));
			}
			TypedQuery<Tuple> typedQuery = entityManager.createQuery(query.where(predicateList.toArray(new Predicate[0])));
			return typedQuery.getResultList().size();
		}catch (Exception e){
			throw new CountyAssessmentException("Error: "+e.getMessage());
		}
	}




}
