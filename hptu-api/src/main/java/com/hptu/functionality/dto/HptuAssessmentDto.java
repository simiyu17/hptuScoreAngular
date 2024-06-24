package com.hptu.functionality.dto;


import java.io.Serializable;
import java.time.LocalDate;

public record HptuAssessmentDto(
         String countyCode,

         LocalDate assessmentDate,

         String functionalityName,

         String questionSummary,

         String questionName,

         Integer attainedScore,

         Integer maxScore,

         String summaryColor
) implements Serializable, Comparable<HptuAssessmentDto> {

    @Override
    public int compareTo(HptuAssessmentDto o) {
        return this.assessmentDate.compareTo(o.assessmentDate());
    }
}
