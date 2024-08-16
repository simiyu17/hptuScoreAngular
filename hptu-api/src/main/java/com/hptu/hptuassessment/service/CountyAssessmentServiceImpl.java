package com.hptu.hptuassessment.service;


import com.hptu.authentication.domain.AppUser;
import com.hptu.functionality.domain.HptuAssessmentMetaData;
import com.hptu.functionality.domain.HptuAssessmentMetaDataRepository;
import com.hptu.hptuassessment.domain.CountyAssessment;
import com.hptu.hptuassessment.domain.CountyAssessmentMetaData;
import com.hptu.hptuassessment.domain.CountyAssessmentMetaDataRepository;
import com.hptu.hptuassessment.exception.CountyAssessmentException;
import com.hptu.report.dto.CountyAssessmentDto;
import com.hptu.report.dto.CountyAssessmentResultDetailedDto;
import com.hptu.report.dto.CountySummaryDto;
import com.hptu.report.dto.HptuCountyAssessmentDto;
import com.hptu.report.dto.ReportByPillar;
import com.hptu.shared.domain.BaseEntity;
import com.hptu.util.CommonUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.hptu.hptuassessment.domain.CountyAssessment.COUNTY_CODE_FIELD;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountyAssessmentServiceImpl implements CountyAssessmentService {

	private final CountyAssessmentMetaDataRepository assessmentRepository;
	private final HptuAssessmentMetaDataRepository hptuAssessmentMetaDataRepository;

	private final EntityManager entityManager;


	@Transactional
	@Override
	public void createCountyAssessment(CountyAssessmentDto countyAssessmentDto){
		try{
		CountyAssessmentMetaData metaData = CountyAssessmentMetaData.createCountyAssessmentMetaData(countyAssessmentDto.assessmentMetaDataDto());
		List<CountyAssessment> assessments = countyAssessmentDto.assessments().stream()
				.map(a -> new CountyAssessment(a.pillarName(), a.category(), a.choiceText(), a.choiceScore(), a.maxScore(), a.scoreRemarks())).toList();
		areAssessmentsValid(assessments);
		metaData.addAssessments(assessments);
            this.assessmentRepository.save(metaData);
        }catch (Exception e){
			throw new CountyAssessmentException(e.getMessage());
		}
	}

	@Override
	public void createCountyHPTUAssessment(HptuCountyAssessmentDto countyAssessmentDto) {
		try {
			hptuAssessmentMetaDataRepository.save(HptuAssessmentMetaData.createAssessments(countyAssessmentDto));
		}catch (Exception e){
			throw new CountyAssessmentException(e.getMessage());
		}
	}

	private void areAssessmentsValid(List<CountyAssessment> assessments){
		if(assessments.isEmpty() || assessments.stream().anyMatch(as -> BigDecimal.ONE.compareTo(as.getChoiceScore()) > 0)){
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
		throw new CountyAssessmentException(e.getMessage());
	}
	}

	@Transactional
	@Override
	public void clearCountyAssessments(Long assessmentId, AppUser user) {
		try{
		CountyAssessmentMetaData status = this.findCountyAssessmentMetaDataById(assessmentId);
		status.getAssessments().clear();
		status.setStatus(CountyAssessmentMetaData.Status.INCOMPLETE);
		status.setLastModifiedBy(user);
	}catch (Exception e){
		throw new CountyAssessmentException(e.getMessage());
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
		List<CountyAssessmentMetaData> assessmentStatuses = this.assessmentRepository.findByCountyCodeAndAssessmentYearAndAssessmentQuarter(countyCode, year, quarter);
		return assessmentStatuses.isEmpty() ? null : assessmentStatuses.getFirst();
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
		typedQuery.getResultList().forEach(t -> result.add(new CountySummaryDto(new CountySummaryDto.Summary("", t.get(0, String.class), t.get(1, BigDecimal.class).setScale(2, RoundingMode.HALF_UP), t.get(2, BigDecimal.class).setScale(2, RoundingMode.HALF_UP), BigDecimal.ONE), false)));
		return  result;
	}catch (Exception e){
		throw new CountyAssessmentException(e.getMessage());
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
		typedQuery.getResultList().forEach(t -> result.add(new CountySummaryDto(new CountySummaryDto.Summary(t.get(0, String.class), "", t.get(1, BigDecimal.class).setScale(2, RoundingMode.HALF_UP), t.get(2, BigDecimal.class).setScale(2, RoundingMode.HALF_UP), BigDecimal.ONE), false)));
		return  result;
	}catch (Exception e){
		throw new CountyAssessmentException(e.getMessage());
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
		throw new CountyAssessmentException(e.getMessage());
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
			BigDecimal countyNumber;
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
				countyNumber = BigDecimal.ONE;
			}

			TypedQuery<Tuple> typedQuery = entityManager.createQuery(query.where(predicateList.toArray(new Predicate[0])));
			List<ReportByPillar.PillarDataPointModel> summaryDataPoints;
			final var countyCount = countyNumber;
			if (groupByPillar){
				typedQuery.getResultList().forEach(t -> countySummaryDtos.add(new CountySummaryDto(new CountySummaryDto.Summary(t.get(0, String.class), "", t.get(1, BigDecimal.class).setScale(2, RoundingMode.HALF_UP), t.get(2, BigDecimal.class).setScale(2, RoundingMode.HALF_UP), countyCount), false)));
				summaryDataPoints = countySummaryDtos.stream()
						.map(s -> new ReportByPillar.PillarDataPointModel(s.getPillarName(), s.getScorePercent())).toList();
			}else {
				typedQuery.getResultList().forEach(t -> countySummaryDtos.add(new CountySummaryDto(new CountySummaryDto.Summary("", t.get(0, String.class), t.get(1, BigDecimal.class).setScale(2, RoundingMode.HALF_UP), t.get(2, BigDecimal.class).setScale(2, RoundingMode.HALF_UP), countyCount), false)));
				summaryDataPoints = countySummaryDtos.stream()
						.map(s -> new ReportByPillar.PillarDataPointModel(s.getCategory(), s.getChoiceScore())).toList();
			}
			return  new CountyAssessmentResultDetailedDto(countySummaryDtos, summaryDataPoints, summaryTitle);
		}catch (Exception e){
			throw new CountyAssessmentException(e.getMessage());
		}
	}

	private BigDecimal getDistinctCountiesCount(String assessmentYear, String assessmentQuarter){
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
			return BigDecimal.valueOf(typedQuery.getResultList().size());
		}catch (Exception e){
			throw new CountyAssessmentException(e.getMessage());
		}
	}




}
