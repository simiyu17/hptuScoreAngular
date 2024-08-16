package com.hptu.functionality.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hptu.report.dto.HptuCountyAssessmentDto;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@Table(name = "county_hptu_assessments_meta_data", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "county_code", "assessment_date" }, name = "HPTU_ASSESSMENT_UNIQUE")
})
public class HptuAssessmentMetaData  extends BaseEntity {

    @Column(name = "county_code", nullable = false)
    private String countyCode;

    @Column(name = "assessment_date", nullable = false)
    private LocalDate assessmentDate;

    @OneToMany(mappedBy = "hptuAssessmentMetaData", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("hptuAssessmentMetaData")
    private List<HptuAssessment> hptuAssessments;

    public HptuAssessmentMetaData() {
    }

    private HptuAssessmentMetaData(String countyCode, LocalDate assessmentDate, List<HptuAssessment> hptuAssessments) {
        this.countyCode = countyCode;
        this.assessmentDate = assessmentDate;
        this.hptuAssessments = hptuAssessments;
    }

    public static HptuAssessmentMetaData createAssessments(final HptuCountyAssessmentDto assessmentDto){
        return new HptuAssessmentMetaData(assessmentDto.countyCode(), LocalDate.parse(assessmentDto.assessmentDate()), HptuAssessment.createAssessments(assessmentDto));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HptuAssessmentMetaData asessment = (HptuAssessmentMetaData) o;

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
