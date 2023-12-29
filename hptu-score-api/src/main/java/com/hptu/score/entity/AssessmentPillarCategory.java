package com.hptu.score.entity;

import com.hptu.score.dto.AssessmentChoiceDto;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "assessment_pillar_categories", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "pillar_id", "category" }, name = "ASSESSMENT_UNIQUE"),
        @UniqueConstraint(columnNames = { "pillar_id", "category_order" }, name = "CAT_NUM_UNIQUE")
})
public class AssessmentPillarCategory extends BaseEntity {

    private static final long serialVersionUID = -7047210967320959597L;

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

    public AssessmentPillarCategory() {
    }
  

    private AssessmentPillarCategory(String category, String choiceOne, int choiceOneScore, String choiceTwo, int choiceTwoScore,
                                     String choiceThree, int choiceThreeScore, String choiceFour, int choiceFourScore, int categoryOrder) {
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

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChoiceOne() {
        return choiceOne;
    }

    public void setChoiceOne(String choiceOne) {
        this.choiceOne = choiceOne;
    }

    public int getChoiceOneScore() {
        return choiceOneScore;
    }

    public void setChoiceOneScore(int choiceOneScore) {
        this.choiceOneScore = choiceOneScore;
    }

    public String getChoiceTwo() {
        return choiceTwo;
    }

    public void setChoiceTwo(String choiceTwo) {
        this.choiceTwo = choiceTwo;
    }

    public int getChoiceTwoScore() {
        return choiceTwoScore;
    }

    public void setChoiceTwoScore(int choiceTwoScore) {
        this.choiceTwoScore = choiceTwoScore;
    }

    public String getChoiceThree() {
        return choiceThree;
    }

    public void setChoiceThree(String choiceThree) {
        this.choiceThree = choiceThree;
    }

    public int getChoiceThreeScore() {
        return choiceThreeScore;
    }

    public void setChoiceThreeScore(int choiceThreeScore) {
        this.choiceThreeScore = choiceThreeScore;
    }

    public String getChoiceFour() {
        return choiceFour;
    }

    public void setChoiceFour(String choiceFour) {
        this.choiceFour = choiceFour;
    }

    public int getChoiceFourScore() {
        return choiceFourScore;
    }

    public void setChoiceFourScore(int choiceFourScore) {
        this.choiceFourScore = choiceFourScore;
    }

    public static AssessmentPillarCategory createAssessmentChoice(AssessmentChoiceDto assessmentChoiceDto){
        return new AssessmentPillarCategory(assessmentChoiceDto.category(), assessmentChoiceDto.choiceOne(), assessmentChoiceDto.choiceOneScore(),
                assessmentChoiceDto.choiceTwo(), assessmentChoiceDto.choiceTwoScore(), assessmentChoiceDto.choiceThree(),
                assessmentChoiceDto.choiceThreeScore(), assessmentChoiceDto.choiceFour(), assessmentChoiceDto.choiceFourScore(),
                assessmentChoiceDto.categoryOrder());
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
