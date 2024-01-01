package com.hptu.score.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CountySummaryDto  {

    private Long metaDataId;
    private String pillarName;

    private String category;

    private int maxScore;

    private int choiceScore;

    private BigDecimal scorePercent;

    private String remark;

    public CountySummaryDto() {
    }

    public CountySummaryDto(Summary summary) {
        this.metaDataId = summary.getMetaDataId();
        this.pillarName = summary.getPillar();
        this.category = summary.getCategory();
        this.maxScore = summary.getMaxScore();
        this.choiceScore = summary.getChoiceScore();
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

    public Long getMetaDataId() {
        return metaDataId;
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

        private final Long metaDataId;
        private String pillar;

        private final String category;

        @JsonProperty("maxscore")
        private final int maxScore;

        @JsonProperty("score")
        private final int choiceScore;

        public Summary(Long metaDataId, String pillar, String category, int maxScore, int choiceScore) {
            this.metaDataId = metaDataId;
            this.pillar = pillar;
            this.category = category;
            this.maxScore = maxScore;
            this.choiceScore = choiceScore;
        }

        public Long getMetaDataId() {
            return metaDataId;
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
    }
}


