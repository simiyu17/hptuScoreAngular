package com.hptu.functionality.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hptu.report.dto.HptuCountyAssessmentDto;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Entity
@Table(name = "county_hptu_assessments")
public class HptuAssessment extends BaseEntity {

    private String functionalityName;

    private String questionSummary;

    private String questionName;

    private BigDecimal attainedScore;

    private BigDecimal maxScore;

    private String summaryColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("hptuAssessmentMetaData")
    private HptuAssessmentMetaData hptuAssessmentMetaData;

    public static final String FUNCTIONALITY_NAME_FIELD = "functionalityName";
    public static final String QUESTION_SUMMARY_FIELD = "questionSummary";
    public static final String META_DATA_ID_FIELD = "hptuAssessmentMetaData";
    public static final String COUNTY_CODE_FIELD = "countyCode";
    public static final String ASSESSMENT_DATE_FIELD = "assessmentDate";
    public static final String ATTAINED_SCORE_FIELD = "attainedScore";
    public static final String MAX_SCORE_FIELD = "maxScore";

    public HptuAssessment() {
    }

    private HptuAssessment(String functionalityName, String questionSummary, String questionName, BigDecimal attainedScore, BigDecimal maxScore, String summaryColor) {
        this.functionalityName = functionalityName;
        this.questionSummary = questionSummary;
        this.questionName = questionName;
        this.attainedScore = attainedScore;
        this.maxScore = maxScore;
        this.summaryColor = summaryColor;
    }

    public static List<HptuAssessment> createAssessments(final HptuCountyAssessmentDto assessmentDto){
        return assessmentDto.assessments().stream()
                .map(assessment -> new HptuAssessment(assessment.functionalityName(), assessment.questionSummary(),
                        assessment.questionName(), assessment.attainedScore(), assessment.maxScore(), assessment.summaryColor())).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HptuAssessment asessment = (HptuAssessment) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), asessment.getHptuAssessmentMetaData())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode()).append(getId()).append(getHptuAssessmentMetaData()).toHashCode();
    }
}
