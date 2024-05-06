package com.hptu.score.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "assessment_pillars", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "pillar_name"}, name = "PILLAR_UNIQUE"),
        @UniqueConstraint(columnNames = { "pillar_order"}, name = "PILLAR_ORDER_UNIQUE")})
public class AssessmentPillar extends BaseEntity implements Comparable<AssessmentPillar>{

    private static final long serialVersionUID = -191798283482066618L;

    @NotBlank
	@Column(name = "pillar_name", length = 2048)
    private String pillarName;

    @Min(1)
    @Column(name = "pillar_order")
    private int pillarOrder;

    @JsonIgnore
    @OneToMany(mappedBy = "pillar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AssessmentPillarCategory> assessmentChoices = new HashSet<>();
    
    @Transient
    private int pillarCategoryCount;

    public AssessmentPillar() {
    }

    private AssessmentPillar(String pillarName, int pillarOrder) {
        this.pillarName = pillarName;
        this.pillarOrder = pillarOrder;
    }
    public Map<String, Object> toFormMap() {
        Map<String, Object> uMap = new HashMap<>();
        uMap.put("id", getId());
        uMap.put("pillarName", getPillarName() == null ? "" : getPillarName());
        uMap.put("pillarOrder", getPillarOrder());
        return uMap;
    }


    public String getPillarName() {
        return pillarName;
    }

    public void setPillarName(String pillarName) {
        this.pillarName = pillarName;
    }

    public int getPillarOrder() {
        return pillarOrder;
    }

    public void setPillarOrder(int pillarOrder) {
        this.pillarOrder = pillarOrder;
    }

    public Set<AssessmentPillarCategory> getAssessmentChoices() {
        return assessmentChoices;
    }

    public void setAssessmentChoices(Set<AssessmentPillarCategory> assessmentChoices) {
        this.assessmentChoices = assessmentChoices;
    }
    
    public void addAssessmentChoice(AssessmentPillarCategory assessmentChoice) {
        assessmentChoice.setPillar(this);
        this.getAssessmentChoices().add(assessmentChoice);
    }

    public static AssessmentPillar createAssessmentPillar(String name, int order){
        return new AssessmentPillar(name, order);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AssessmentPillar pillar = (AssessmentPillar) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), pillar.getId()).append(getPillarName(), pillar.getPillarName())
                .append(getPillarOrder(), pillar.getPillarOrder()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode()).append(getId()).append(getPillarName())
                .append(getPillarOrder()).toHashCode();
    }

    /**
     * @return the pillarCategoryCount
     */
    public int getPillarCategoryCount() {
        return this.getAssessmentChoices().size();
    }


    @Override
    public int compareTo(AssessmentPillar o) {
        return Integer.compare(this.getPillarOrder(), o.getPillarOrder());
    }
}
