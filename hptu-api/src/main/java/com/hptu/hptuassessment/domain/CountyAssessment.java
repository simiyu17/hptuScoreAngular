package com.hptu.hptuassessment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

@Getter
@NamedQuery(name = "County_Summary",
        query = "select ca.pillarName, sum(ca.maxScore) as maxScore, sum(ca.choiceScore) as score from  CountyAssessment ca where ca.metaData.id = ?1 group by 1")
@NamedQuery(name = "Pillar_Summary",
        query = "select ca.category, sum(ca.maxScore) as maxScore, sum(ca.choiceScore) as score from  CountyAssessment ca where ca.metaData.id = ?1 and ca.pillarName = ?2 group by 1")
@Entity
@Table(name = "county_assessment", uniqueConstraints = { @UniqueConstraint(columnNames = { "meta_data_id", "pillar_name", "category" }, name = "COUNTY_ASSESSMENT_UNIQUE")})
public class CountyAssessment extends BaseEntity {

    @Column(name = "pillar_name")
    private String pillarName;

    @Column(name = "category")
    private String category;

    @Column(name = "choice_text", length = 2048)
    private String choiceText;

    private BigDecimal choiceScore;

    private BigDecimal maxScore;

    private String scoreRemarks;

    public static final String PILLAR_FIELD = "pillarName";
    public static final String CATEGORY_FIELD = "category";
    public static final String CHOICE_SCORE_FIELD = "choiceScore";
    public static final String MAX_SCORE_FIELD = "maxScore";
    public static final String META_DATA_ID_FIELD = "metaData";
    public static final String COUNTY_CODE_FIELD = "countyCode";
    
    @Setter
    @ManyToOne
    @JoinColumn(name = "meta_data_id")
    @JsonIgnore
    private CountyAssessmentMetaData metaData;
    
    public CountyAssessment() {
	}

    public CountyAssessment(String pillarName, String category, String choiceText, BigDecimal choiceScore, BigDecimal maxScore, String scoreRemarks) {
		super();
		this.pillarName = pillarName;
		this.category = category;
		this.choiceText = choiceText;
		this.choiceScore = choiceScore;
        this.maxScore = maxScore;
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
