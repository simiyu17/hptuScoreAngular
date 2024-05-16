package com.hptu.score.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CountySummaryDto  {

    private String pillarName;

    private String category;

    private int maxScore;

    private int choiceScore;

    private BigDecimal scorePercent;

    private String remark;

    public CountySummaryDto() {
    }

    public CountySummaryDto(Summary summary) {
        this.pillarName = summary.getPillar();
        this.category = summary.getCategory();
        this.maxScore = summary.getMaxScore();
        this.choiceScore = summary.getChoiceScore();
        if (summary.getCountiesNumber() > 1){
            this.maxScore = (new BigDecimal(this.choiceScore)).divide(new BigDecimal(summary.getCountiesNumber()), 4, RoundingMode.HALF_UP).intValue();
            this.choiceScore = (new BigDecimal(this.choiceScore)).divide(new BigDecimal(summary.getCountiesNumber()), 4, RoundingMode.HALF_UP).intValue();
        }
        BigDecimal nom = new BigDecimal(this.choiceScore);
        BigDecimal den = new BigDecimal(this.maxScore);
        BigDecimal p100 = new BigDecimal(100);
        this.scorePercent = den.compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ZERO : (nom.divide(den, 8, RoundingMode.HALF_UP)).multiply(p100).setScale(2, RoundingMode.HALF_UP);
        if (this.scorePercent.compareTo(new BigDecimal(80)) >= 0){
            this.remark = "Excellent";
        } else if (this.scorePercent.compareTo(new BigDecimal(65)) >= 0) {
            this.remark = "Good";
        }else if (this.scorePercent.compareTo(new BigDecimal(50)) >= 0) {
            this.remark = "Average";
        }else {
            this.remark = "Below Average";
        }
    }

    public String getPillarName() {
        return pillarName;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getChoiceScore() {
        return choiceScore;
    }

    public BigDecimal getScorePercent() {
        return scorePercent;
    }

    public String getRemark() {
        return remark;
    }

    public String getCategory() {
        return category;
    }

    public static class Summary {

        private String pillar;

        private final String category;

        @JsonProperty("maxscore")
        private final int maxScore;

        @JsonProperty("score")
        private final int choiceScore;

        private final int countiesNumber;

        public Summary(String pillar, String category, int maxScore, int choiceScore, int countiesNumber) {
            this.pillar = pillar;
            this.category = category;
            this.maxScore = maxScore;
            this.choiceScore = choiceScore;
            this.countiesNumber = countiesNumber;
        }

        public String getPillar() {
            return pillar;
        }

        public void setPillar(String pillar) {
            this.pillar = pillar;
        }

        public int getMaxScore() {
            return maxScore;
        }

        public int getChoiceScore() {
            return choiceScore;
        }

        public String getCategory() {
            return category;
        }

        public int getCountiesNumber() {
            return countiesNumber;
        }
    }
}


