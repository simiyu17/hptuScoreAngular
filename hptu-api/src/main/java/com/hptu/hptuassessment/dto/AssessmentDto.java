package com.hptu.hptuassessment.dto;

import java.math.BigDecimal;

public record AssessmentDto(String pillarName, Long pillarId, String category, String choiceText,
                            BigDecimal choiceScore, BigDecimal maxScore, String scoreRemarks) {

}
