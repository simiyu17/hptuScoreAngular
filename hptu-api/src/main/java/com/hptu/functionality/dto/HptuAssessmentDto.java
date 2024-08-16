package com.hptu.functionality.dto;


import java.io.Serializable;
import java.math.BigDecimal;

public record HptuAssessmentDto(

         String functionalityName,

         String questionSummary,

         String questionName,

         BigDecimal attainedScore,

         BigDecimal maxScore,

         String summaryColor,

         Long questionSummaryId,

         Long functionalityId
) implements Serializable {}
