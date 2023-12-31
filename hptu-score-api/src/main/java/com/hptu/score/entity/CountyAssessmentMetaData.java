package com.hptu.score.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hptu.score.dto.CountyAssessmentMetaDataDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "assessment_meta_data", uniqueConstraints = { @UniqueConstraint(columnNames = { "assess_quarter", "ass_year", "county_code" }, name = "ASSESSMENT_STATUS_UNIQUE")})
public class CountyAssessmentMetaData extends BaseEntity {
	
	private static final long serialVersionUID = 4983928165704865513L;

	@Column(name = "assess_quarter")
    private String assessmentQuarter;

    @Column(name = "ass_year")
    private String assessmentYear;
    
    @Column(name = "ass_level")
    private String assessmentLevel;

    @Column(name = "county_code")
    private String countyCode;

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
	
	public void addAssessments(List<CountyAssessment> assessments) {
		assessments.forEach(as ->{
			as.setMetaData(this);
			this.getAssessments().add(as);
		});
	}


public static CountyAssessmentMetaData createCountyAssessmentMetaData(CountyAssessmentMetaDataDto metaDataDto){
		return new CountyAssessmentMetaData(metaDataDto.getAssessmentQuarter(), metaDataDto.getAssessmentYear(), metaDataDto.getAssessmentLevel(), metaDataDto.getCountyCode(), Status.COMPLETE);
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
