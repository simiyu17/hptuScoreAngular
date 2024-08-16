package com.hptu.report.dto;

import com.hptu.functionality.dto.HptuAssessmentDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;


public record HptuCountyAssessmentDto(@NotNull String countyCode,
                                      @NotNull String assessmentDate,
                                      @Size(min = 1) List<HptuAssessmentDto> assessments) implements Serializable {


}
