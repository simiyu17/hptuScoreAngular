package com.hptu.hptuassessment.domain;

import com.hptu.report.dto.CountyAssessmentMetaDataDto;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "assessment_meta_data", uniqueConstraints = { @UniqueConstraint(columnNames = { "assess_quarter", "ass_year", "county_code" }, name = "ASSESSMENT_STATUS_UNIQUE")})
public class CountyAssessmentMetaData extends BaseEntity {

	@Column(name = "assess_quarter")
    private String assessmentQuarter;

    @Column(name = "ass_year")
    private String assessmentYear;
    
    @Column(name = "ass_level")
    private String assessmentLevel;

    @Column(name = "county_code")
    private String countyCode;

	@Transient
	private String countyName;

    @Enumerated(EnumType.STRING)
	private Status status;
	
	@OneToMany(mappedBy = "metaData", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CountyAssessment> assessments;

	public CountyAssessmentMetaData() {
	}

	private CountyAssessmentMetaData(String assessmentQuarter, String assessmentYear, String assessmentLevel, String countyCode, Status status) {
		this.assessmentQuarter = assessmentQuarter;
		this.assessmentYear = assessmentYear;
		this.assessmentLevel = assessmentLevel;
		this.countyCode = countyCode;
		this.status = status;
		assessments = new HashSet<>();
	}

	public String getAssessmentQuarter() {
		return assessmentQuarter;
	}




	public void setAssessmentQuarter(String assessmentQuarter) {
		this.assessmentQuarter = assessmentQuarter;
	}




	public String getAssessmentYear() {
		return assessmentYear;
	}




	public void setAssessmentYear(String assessmentYear) {
		this.assessmentYear = assessmentYear;
	}




	public String getCountyCode() {
		return countyCode;
	}




	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}




	public Status getStatus() {
		return status;
	}




	public void setStatus(Status status) {
		this.status = status;
	}




	public Set<CountyAssessment> getAssessments() {
		return assessments;
	}




	public void setAssessments(Set<CountyAssessment> assessments) {
		this.assessments = assessments;
	}
	
	
	
	public String getAssessmentLevel() {
		return assessmentLevel;
	}




	public void setAssessmentLevel(String assessmentLevel) {
		this.assessmentLevel = assessmentLevel;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public void addAssessments(List<CountyAssessment> assessments) {
		assessments.forEach(as ->{
			as.setMetaData(this);
			this.getAssessments().add(as);
		});
	}


public static CountyAssessmentMetaData createCountyAssessmentMetaData(CountyAssessmentMetaDataDto metaDataDto){
		return new CountyAssessmentMetaData(metaDataDto.assessmentQuarter(), metaDataDto.assessmentYear(), metaDataDto.assessmentLevel(), metaDataDto.countyCode(), Status.COMPLETE);
}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CountyAssessmentMetaData countyAssessmentStatus = (CountyAssessmentMetaData) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), countyAssessmentStatus.getId())
                .append(getAssessmentYear(), countyAssessmentStatus.getAssessmentYear())
                .append(getCountyCode(), countyAssessmentStatus.getCountyCode())
                .append(getAssessmentQuarter(), countyAssessmentStatus.getAssessmentQuarter()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getId()).append(getAssessmentYear())
                .append(getCountyCode())
                .append(getAssessmentQuarter()).toHashCode();
    }




	public enum Status{
		INCOMPLETE, COMPLETE
	}
}
