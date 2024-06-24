package com.hptu.functionality.domain;

import com.hptu.functionality.dto.HptuAssessmentDto;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.util.List;


@Getter
@Entity
@Table(name = "assessments", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "county_code", "assessment_date" }, name = "HPTU_ASSESSMENT_UNIQUE")
})
public class HptuAssessment extends BaseEntity {

    @Column(name = "county_code", nullable = false)
    private String countyCode;

    @Column(name = "assessment_date", nullable = false)
    private LocalDate assessmentDate;

    private String functionalityName;

    private String questionSummary;

    private String questionName;

    private Integer attainedScore;

    private Integer maxScore;

    private String summaryColor;

    public HptuAssessment() {
    }

    private HptuAssessment(String countyCode, LocalDate assessmentDate, String functionalityName, String questionSummary, String questionName, Integer attainedScore, Integer maxScore, String summaryColor) {
        this.countyCode = countyCode;
        this.assessmentDate = assessmentDate;
        this.functionalityName = functionalityName;
        this.questionSummary = questionSummary;
        this.questionName = questionName;
        this.attainedScore = attainedScore;
        this.maxScore = maxScore;
        this.summaryColor = summaryColor;
    }

    public static HptuAssessment createAssessment(HptuAssessmentDto assessmentDto){
        return new HptuAssessment(assessmentDto.countyCode(), assessmentDto.assessmentDate(), assessmentDto.functionalityName(), assessmentDto.questionSummary(), assessmentDto.questionName(), assessmentDto.attainedScore(), assessmentDto.maxScore(), assessmentDto.summaryColor());
    }

    public static List<HptuAssessment> createAssessments(List<HptuAssessmentDto> assessmentDtos){
        return assessmentDtos.stream()
                .map(assessmentDto -> new HptuAssessment(assessmentDto.countyCode(), assessmentDto.assessmentDate(), assessmentDto.functionalityName(), assessmentDto.questionSummary(),
                        assessmentDto.questionName(), assessmentDto.attainedScore(), assessmentDto.maxScore(), assessmentDto.summaryColor())).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HptuAssessment asessment = (HptuAssessment) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), asessment.getCountyCode())
                .append(getAssessmentDate(), asessment.getAssessmentDate())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode()).append(getId()).append(getCountyCode()).append(getAssessmentDate()).toHashCode();
    }
}
