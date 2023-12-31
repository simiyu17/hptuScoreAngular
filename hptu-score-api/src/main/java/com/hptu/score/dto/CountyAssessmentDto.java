package com.hptu.score.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CountyAssessmentDto {

    @NotNull
    private CountyAssessmentMetaDataDto assessmentMetaDataDto;
    @Size(min = 1)
    private List<AssessmentDto> assessments;

    public CountyAssessmentMetaDataDto getAssessmentMetaDataDto() {
        return assessmentMetaDataDto;
    }

    public void setAssessmentMetaDataDto(CountyAssessmentMetaDataDto assessmentMetaDataDto) {
        this.assessmentMetaDataDto = assessmentMetaDataDto;
    }

    public List<AssessmentDto> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<AssessmentDto> assessments) {
        this.assessments = assessments;
    }
}
