package com.hptu.report.dto;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class HptuCountySummaryDto implements Serializable {

    private String functionalityName;

    private String questionSummary;

    private String questionName;

    private BigDecimal attainedScore;

    private BigDecimal maxScore;

    private String summaryColor;

    public HptuCountySummaryDto() {
    }

    public HptuCountySummaryDto(String functionalityName, String questionSummary, String questionName, BigDecimal maxScore, BigDecimal attainedScore, String summaryColor) {
        this.functionalityName = functionalityName;
        this.questionSummary = questionSummary;
        this.questionName = questionName;
        this.attainedScore = attainedScore;
        this.maxScore = maxScore;
        this.summaryColor = summaryColor;
    }
}


