package com.hptu.score.entity;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "county_assessment", uniqueConstraints = { @UniqueConstraint(columnNames = { "status_id", "pillar", "category" }, name = "COUNTY_ASSESSMENT_UNIQUE")})
public class CountyAssessment extends BaseEntity {

    private static final long serialVersionUID = -6859602933138173920L;

    @Column(name = "pillar")
    private String pillar;

    @Column(name = "category")
    private String category;

    @Column(name = "choice_text", length = 2048)
    private String choiceText;

    private int choiceScore;

    private int maxScore;

    private String scoreRemarks;
    
    @ManyToOne
    @JoinColumn(name = "status_id")
    private CountyAssessmentStatus status;
    
    public CountyAssessment() {
	}

    public CountyAssessment(String pillar, String category, String choiceText, int choiceScore, int maxScore, String scoreRemarks) {
		super();
		this.pillar = pillar;
		this.category = category;
		this.choiceText = choiceText;
		this.choiceScore = choiceScore;
        this.maxScore = maxScore;
        this.scoreRemarks = scoreRemarks;
	}

	public String getPillar() {
        return pillar;
    }

    public void setPillar(String pillar) {
        this.pillar = pillar;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public int getChoiceScore() {
        return choiceScore;
    }

    public void setChoiceScore(int choiceScore) {
        this.choiceScore = choiceScore;
    }

    public CountyAssessmentStatus getStatus() {
		return status;
	}

	public void setStatus(CountyAssessmentStatus status) {
		this.status = status;
	}

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getScoreRemarks() {
        return scoreRemarks;
    }

    public void setScoreRemarks(String scoreRemarks) {
        this.scoreRemarks = scoreRemarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CountyAssessment countyAssessment = (CountyAssessment) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), countyAssessment.getId())
                .append(getStatus(), countyAssessment.getStatus())
                .append(getPillar(), countyAssessment.getPillar())
                .append(getCategory(), countyAssessment.getCategory()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getId()).append(getStatus())
                .append(getPillar())
                .append(getCategory()).toHashCode();
    }
}
