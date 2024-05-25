package com.hptu.setup.domain;

import com.hptu.setup.dto.AssessmentChoiceDto;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

@Entity
@Table(name = "assessment_pillar_categories", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "pillar_id", "category" }, name = "ASSESSMENT_UNIQUE"),
        @UniqueConstraint(columnNames = { "pillar_id", "category_order" }, name = "CAT_NUM_UNIQUE")
})
public class AssessmentPillarCategory extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pillar_id")
    private AssessmentPillar pillar;
    
    @Transient
    private Long pillarId;

    @Column(name = "category", length = 2048)
    private String category;

    @Column(name = "choice_one", length = 2048)
    private String choiceOne;

    private int choiceOneScore;

    @Column(name = "choice_two", length = 2048)
    private String choiceTwo;

    private int choiceTwoScore;

    @Column(name = "choice_three", length = 2048)
    private String choiceThree;

    private int choiceThreeScore;

    @Column(name = "choice_four", length = 2048)
    private String choiceFour;

    private int choiceFourScore;
    
    @Column(name = "category_order")
    private int categoryOrder;

    private String allowedQuarters;

    public AssessmentPillarCategory() {
    }
  

    private AssessmentPillarCategory(String category, String choiceOne, int choiceOneScore, String choiceTwo, int choiceTwoScore,
                                     String choiceThree, int choiceThreeScore, String choiceFour, int choiceFourScore, int categoryOrder, List<String> allowedQuarters) {
        this.category = category;
        this.choiceOne = choiceOne;
        this.choiceOneScore = choiceOneScore;
        this.choiceTwo = choiceTwo;
        this.choiceTwoScore = choiceTwoScore;
        this.choiceThree = choiceThree;
        this.choiceThreeScore = choiceThreeScore;
        this.choiceFour = choiceFour;
        this.choiceFourScore = choiceFourScore;
        this.categoryOrder = categoryOrder;
        this.allowedQuarters = allowedQuarters.toString();
    }
    
    

    public AssessmentPillar getPillar() {
        return pillar;
    }

    public void setPillar(AssessmentPillar pillar) {
        this.pillar = pillar;
    }

    public String getCategory() {
        return category;
    }

    public String getChoiceOne() {
        return choiceOne;
    }

    public int getChoiceOneScore() {
        return choiceOneScore;
    }

    public String getChoiceTwo() {
        return choiceTwo;
    }

    public int getChoiceTwoScore() {
        return choiceTwoScore;
    }

    public String getChoiceThree() {
        return choiceThree;
    }

    public int getChoiceThreeScore() {
        return choiceThreeScore;
    }

    public String getChoiceFour() {
        return choiceFour;
    }

    public int getChoiceFourScore() {
        return choiceFourScore;
    }

    public String getAllowedQuarters() {
        return allowedQuarters;
    }

    public static AssessmentPillarCategory createAssessmentChoice(AssessmentChoiceDto assessmentChoiceDto){
        return new AssessmentPillarCategory(assessmentChoiceDto.category(), assessmentChoiceDto.choiceOne(), assessmentChoiceDto.choiceOneScore(),
                assessmentChoiceDto.choiceTwo(), assessmentChoiceDto.choiceTwoScore(), assessmentChoiceDto.choiceThree(),
                assessmentChoiceDto.choiceThreeScore(), assessmentChoiceDto.choiceFour(), assessmentChoiceDto.choiceFourScore(),
                assessmentChoiceDto.categoryOrder(), assessmentChoiceDto.allowedQuarters());
    }

    public void updateCategory(AssessmentChoiceDto categoryUpdate){
        if(!StringUtils.equals(categoryUpdate.category(), this.category)){
            this.category = categoryUpdate.category();
        }
        if(categoryUpdate.categoryOrder() !=  this.categoryOrder){
            this.categoryOrder = categoryUpdate.categoryOrder();
        }

        if(!StringUtils.equals(categoryUpdate.choiceOne(), this.choiceOne)){
            this.choiceOne = categoryUpdate.choiceOne();
        }
        if(categoryUpdate.choiceOneScore() !=  this.choiceOneScore){
            this.choiceOneScore = categoryUpdate.choiceOneScore();
        }

        if(!StringUtils.equals(categoryUpdate.choiceTwo(), this.choiceTwo)){
            this.choiceTwo = categoryUpdate.choiceTwo();
        }
        if(categoryUpdate.choiceTwoScore()!=  this.choiceTwoScore){
            this.choiceTwoScore = categoryUpdate.choiceTwoScore();
        }

        if(!StringUtils.equals(categoryUpdate.choiceThree(), this.choiceThree)){
            this.choiceThree = categoryUpdate.choiceThree();
        }
        if(categoryUpdate.choiceThreeScore()!=  this.choiceThreeScore){
            this.choiceThreeScore = categoryUpdate.choiceThreeScore();
        }

        if(!StringUtils.equals(categoryUpdate.choiceFour(), this.choiceFour)){
            this.choiceFour = categoryUpdate.choiceFour();
        }
        if(categoryUpdate.choiceFourScore() !=  this.choiceFourScore){
            this.choiceFourScore = categoryUpdate.choiceFourScore();
        }
        if(!categoryUpdate.allowedQuarters().isEmpty()){
            this.allowedQuarters = categoryUpdate.allowedQuarters().toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AssessmentPillarCategory choice = (AssessmentPillarCategory) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), choice.getId())
                .append(getPillar(), choice.getPillar())
                .append(getCategory(), choice.getCategory()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode()).append(getId()).append(getPillar())
                .append(getCategory()).toHashCode();
    }

    /**
     * @return the pillarId
     */
    public Long getPillarId() {
        return pillarId;
    }

    /**
     * @param pillarId the pillarId to set
     */
    public void setPillarId(Long pillarId) {
        this.pillarId = pillarId;
    }


	public int getCategoryOrder() {
		return categoryOrder;
	}


	public void setCategoryOrder(int categoryOrder) {
		this.categoryOrder = categoryOrder;
	}

    
    
}
