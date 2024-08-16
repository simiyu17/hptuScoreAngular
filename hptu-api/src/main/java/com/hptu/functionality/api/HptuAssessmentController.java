package com.hptu.functionality.api;

import com.hptu.hptuassessment.service.CountyAssessmentService;
import com.hptu.report.dto.HptuCountyAssessmentDto;
import com.hptu.shared.dto.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/county-hptu-assessments")
public class HptuAssessmentController {

    private final CountyAssessmentService countyAssessmentService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> createCountAssessment(@Valid @RequestBody HptuCountyAssessmentDto newAssessment) {
        try {
            this.countyAssessmentService.createCountyHPTUAssessment(newAssessment);
            return new ResponseEntity<>(new ApiResponseDto(true, "County assessment was submitted!!"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDto(true, "An Error Occurred: "+e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }
}
