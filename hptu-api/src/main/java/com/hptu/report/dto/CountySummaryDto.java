package com.hptu.report.dto;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class CountySummaryDto implements Serializable {

    private String pillarName;

    private String category;

    private BigDecimal maxScore;

    private BigDecimal choiceScore;

    private BigDecimal scorePercent;

    private String remark;

    public CountySummaryDto() {
    }

    public CountySummaryDto(Summary summary, boolean isVersion2) {
        this.pillarName = summary.pillar();
        this.category = summary.category();
        this.maxScore = summary.maxScore();
        this.choiceScore = summary.choiceScore();
        if (BigDecimal.ZERO.compareTo(summary.countiesNumber()) < 0 && !isVersion2){
            this.maxScore = (this.choiceScore).divide(summary.countiesNumber(), 4, RoundingMode.HALF_UP);
            this.choiceScore = (this.choiceScore).divide(summary.countiesNumber(), 4, RoundingMode.HALF_UP);
        }
        BigDecimal nom = this.choiceScore;
        BigDecimal den = this.maxScore;
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



    public record Summary(String pillar, String category, BigDecimal maxScore, BigDecimal choiceScore,
                          BigDecimal countiesNumber) {

    }
}


