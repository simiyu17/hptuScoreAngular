package com.hptu.report.dto;

import com.hptu.hptuassessment.dto.AssessmentDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;


public record CountyAssessmentDto(@NotNull CountyAssessmentMetaDataDto assessmentMetaDataDto,
                                  @Size(min = 1) List<AssessmentDto> assessments) implements Serializable {


}
