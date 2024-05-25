package com.hptu.score.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@NamedQuery(name = "County_Summary",
        query = "select ca.pillarName, sum(ca.maxScore) as maxScore, sum(ca.choiceScore) as score from  CountyAssessment ca where ca.metaData.id = ?1 group by 1")
@NamedQuery(name = "Pillar_Summary",
        query = "select ca.category, sum(ca.maxScore) as maxScore, sum(ca.choiceScore) as score from  CountyAssessment ca where ca.metaData.id = ?1 and ca.pillarName = ?2 group by 1")
@Entity
@Table(name = "county_assessment", uniqueConstraints = { @UniqueConstraint(columnNames = { "meta_data_id", "pillar_name", "category" }, name = "COUNTY_ASSESSMENT_UNIQUE")})
public class CountyAssessment extends BaseEntity {

    private static final long serialVersionUID = -6859602933138173920L;

    @Column(name = "pillar_name")
    private String pillarName;

    @Column(name = "category")
    private String category;

    @Column(name = "choice_text", length = 2048)
    private String choiceText;

    private int choiceScore;

    private int maxScore;

    private String scoreRemarks;

    public static final String PILLAR_FIELD = "pillarName";
    public static final String CATEGORY_FIELD = "category";
    public static final String CHOICE_SCORE_FIELD = "choiceScore";
    public static final String MAX_SCORE_FIELD = "maxScore";
    public static final String META_DATA_ID_FIELD = "metaData";
    public static final String COUNTY_CODE_FIELD = "countyCode";
    
    @ManyToOne
    @JoinColumn(name = "meta_data_id")
    @JsonIgnore
    private CountyAssessmentMetaData metaData;
    
    public CountyAssessment() {
	}

    public CountyAssessment(String pillarName, String category, String choiceText, int choiceScore, int maxScore, String scoreRemarks) {
		super();
		this.pillarName = pillarName;
		this.category = category;
		this.choiceText = choiceText;
		this.choiceScore = choiceScore;
        this.maxScore = maxScore;
        this.scoreRemarks = scoreRemarks;
	}

    public String getPillarName() {
        return pillarName;
    }

    public void setPillarName(String pillarName) {
        this.pillarName = pillarName;
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

    public CountyAssessmentMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(CountyAssessmentMetaData metaData) {
        this.metaData = metaData;
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
                .append(getMetaData(), countyAssessment.getMetaData())
                .append(getPillarName(), countyAssessment.getPillarName())
                .append(getCategory(), countyAssessment.getCategory()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getId()).append(getMetaData())
                .append(getPillarName())
                .append(getCategory()).toHashCode();
    }
}
