package com.hptu.functionality.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hptu.functionality.dto.FunctionalityDto;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "hptu_functionality", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "hptu_name" }, name = "HPTU_Q_UNIQUE"),
        @UniqueConstraint(columnNames = { "functionality_order" }, name = "HPTU_ORDER_UNIQUE")
})
public class Functionality extends BaseEntity {

    @Column(name = "hptu_name", nullable = false, length = 2048)
    private String hptuName;

    @Column(length = 2048)
    private String description;

    @Column(name = "functionality_order")
    private int functionalityOrder;

    @OneToMany(mappedBy = "functionality", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("functionality")
    private Set<QuestionSummary> questions = new HashSet<>();

    public Functionality() {
    }

    private Functionality(String hptuName, String description, int functionalityOrder) {
        this.hptuName = hptuName;
        this.description = description;
        this.functionalityOrder = functionalityOrder;
    }


    public static Functionality createFunctionality(FunctionalityDto functionalityDto){
        return new Functionality(functionalityDto.hptuName(), functionalityDto.description(), functionalityDto.functionalityOrder());
    }

    public void updateFunctionality(FunctionalityDto functionalityDto){
        if(!StringUtils.equals(functionalityDto.hptuName(), this.hptuName)){
            this.hptuName = functionalityDto.hptuName();
        }
        if(!StringUtils.equals(functionalityDto.description(), this.description)){
            this.description = functionalityDto.description();
        }
    }

    public void addQuestionSummary(final QuestionSummary questionSummary){
        this.getQuestions().add(questionSummary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Functionality functionality = (Functionality) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), functionality.getId())
                .append(getHptuName(), functionality.getHptuName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode()).append(getId()).append(getHptuName()).toHashCode();
    }

}
