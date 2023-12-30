package com.hptu.score.dto;

import java.util.List;

public class CountyAssessmentDto2 {

    private CountyAssessmentStatusDto assessmentStatus;

    private List<AssessmentDto> assessments;

    public CountyAssessmentStatusDto getAssessmentStatus() {
        return assessmentStatus;
    }

    public void setAssessmentStatus(CountyAssessmentStatusDto assessmentStatus) {
        this.assessmentStatus = assessmentStatus;
    }

    public List<AssessmentDto> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<AssessmentDto> assessments) {
        this.assessments = assessments;
    }
}
