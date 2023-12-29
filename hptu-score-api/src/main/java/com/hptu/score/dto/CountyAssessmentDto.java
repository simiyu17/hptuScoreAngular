package com.hptu.score.dto;

import java.io.Serializable;

public class CountyAssessmentDto implements Serializable {

	 	private static final long serialVersionUID = -988728442741567130L;

		private Long statusId;
	 	
	 	private String pillar;

	    private String category;
	    
	    private String choiceText1;

	    private int choiceScore1;
	    
	    private String choiceText2;

	    private int choiceScore2;
	    
	    private String choiceText3;

	    private int choiceScore3;
	    
	    private String choiceText4;

	    private int choiceScore4;

	    private String choiceText;

	    private int choiceScore;

		private int maxScore;
	    
	    private int currentPillarOrder;

		private int categoryOrder;

		private String scoreRemarks;
	    
	    public CountyAssessmentDto() {
		}

		public CountyAssessmentDto(Long statusId, String pillar, String category, String choiceText1, int choiceScore1,
				String choiceText2, int choiceScore2, String choiceText3, int choiceScore3, String choiceText4,
			   int choiceScore4, int currentPillarOrder, int categoryOrder, int maxScore, String scoreRemarks) {
			this.statusId = statusId;
			this.pillar = pillar;
			this.category = category;
			this.choiceText1 = choiceText1;
			this.choiceScore1 = choiceScore1;
			this.choiceText2 = choiceText2;
			this.choiceScore2 = choiceScore2;
			this.choiceText3 = choiceText3;
			this.choiceScore3 = choiceScore3;
			this.choiceText4 = choiceText4;
			this.choiceScore4 = choiceScore4;
			this.currentPillarOrder = currentPillarOrder;
			this.categoryOrder = categoryOrder;
			this.maxScore = maxScore;
			this.scoreRemarks = scoreRemarks;
		}

		public Long getStatusId() {
			return statusId;
		}

		public void setStatusId(Long statusId) {
			this.statusId = statusId;
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

		public String getChoiceText1() {
			return choiceText1;
		}

		public void setChoiceText1(String choiceText1) {
			this.choiceText1 = choiceText1;
		}

		public int getChoiceScore1() {
			return choiceScore1;
		}

		public void setChoiceScore1(int choiceScore1) {
			this.choiceScore1 = choiceScore1;
		}

		public String getChoiceText2() {
			return choiceText2;
		}

		public void setChoiceText2(String choiceText2) {
			this.choiceText2 = choiceText2;
		}

		public int getChoiceScore2() {
			return choiceScore2;
		}

		public void setChoiceScore2(int choiceScore2) {
			this.choiceScore2 = choiceScore2;
		}

		public String getChoiceText3() {
			return choiceText3;
		}

		public void setChoiceText3(String choiceText3) {
			this.choiceText3 = choiceText3;
		}

		public int getChoiceScore3() {
			return choiceScore3;
		}

		public void setChoiceScore3(int choiceScore3) {
			this.choiceScore3 = choiceScore3;
		}

		public String getChoiceText4() {
			return choiceText4;
		}

		public void setChoiceText4(String choiceText4) {
			this.choiceText4 = choiceText4;
		}

		public int getChoiceScore4() {
			return choiceScore4;
		}

		public void setChoiceScore4(int choiceScore4) {
			this.choiceScore4 = choiceScore4;
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

		public int getCurrentPillarOrder() {
			return currentPillarOrder;
		}

		public void setCurrentPillarOrder(int currentPillarOrder) {
			this.currentPillarOrder = currentPillarOrder;
		}

	public int getCategoryOrder() {
		return categoryOrder;
	}

	public void setCategoryOrder(int categoryOrder) {
		this.categoryOrder = categoryOrder;
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
}
