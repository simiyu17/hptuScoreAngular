package com.hptu.score.dto;

import java.util.ArrayList;
import java.util.List;

public class AssessmentDataCreationDto {

	List<CountyAssessmentDto> assessments;
	
	public AssessmentDataCreationDto() {
		this.assessments = new ArrayList<>();
	}

	public AssessmentDataCreationDto(List<CountyAssessmentDto> hfDataList) {
		this.assessments = hfDataList;
	}

	public List<CountyAssessmentDto> getAssessments() {
		return assessments;
	}

	public void setAssessments(List<CountyAssessmentDto> assessments) {
		this.assessments = assessments;
	}
	
	 public void addAssessmentToList(CountyAssessmentDto assessment) {
	        this.assessments.add(assessment);
	    }

}
