package com.hptu.hptuassessment.api;

import com.hptu.hptuassessment.domain.CountyAssessmentMetaData;
import com.hptu.hptuassessment.service.CountyAssessmentService;
import com.hptu.report.dto.CountyAssessmentDto;
import com.hptu.shared.dto.ApiResponseDto;
import com.hptu.util.CommonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/county-assessments")
public class CountyAssessmentController {

    private final CountyAssessmentService countyAssessmentService;


    @GetMapping
    public ResponseEntity<List<CountyAssessmentMetaData>>  getAvailableAssessments(){
        List<CountyAssessmentMetaData> list = new ArrayList<>();
        long limit = 15;
        for (CountyAssessmentMetaData ass : this.countyAssessmentService.getAvailableCountyAssessmentMetaDatas()) {
            if (limit-- == 0) break;
            ass.setCountyName(CommonUtil.getCountyByCode(ass.getCountyCode()));
            ass.setAssessmentQuarter(getPeriodMonths(ass.getAssessmentQuarter()));
            list.add(ass);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("{assessmentId}")
    public ResponseEntity<CountyAssessmentMetaData> getAssessmentById(@PathVariable("assessmentId") Long assessmentId){
        return new ResponseEntity<>(this.countyAssessmentService.findCountyAssessmentMetaDataById(assessmentId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> createCountAssessment(@Valid @RequestBody CountyAssessmentDto newAssessment) {
        try {
            this.countyAssessmentService.createCountyAssessment(newAssessment);
            return new ResponseEntity<>(new ApiResponseDto(true, "County assessment was submitted!!"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDto(true, "An Error Occurred: "+e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("find-one")
    public ResponseEntity<CountyAssessmentMetaData> getAssessmentById(@RequestParam("countyCode") String countyCode,
                                      @RequestParam("assessmentYear") String assessmentYear,
                                      @RequestParam("assessmentQuarter") String assessmentQuarter){
        var res = this.countyAssessmentService.getCountyAssessmentByCodeYearAndQuarter(countyCode, assessmentQuarter, assessmentYear);
        if(Objects.nonNull(res)) {
            res.setCountyName(CommonUtil.getCountyByCode(res.getCountyCode()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("{assessmentId}")
    public ResponseEntity<ApiResponseDto> deleteAssessmentById(@PathVariable("assessmentId") Long assessmentId){
        countyAssessmentService.deleteCountyAssessmentMetaData(assessmentId);
        return new ResponseEntity<>(new ApiResponseDto(true, "Assessment Deleted !!"), HttpStatus.NO_CONTENT);
    }

    private String getPeriodMonths(String quarterName){
        return switch (quarterName){
            case "Q1" -> "Jan - Mar";
            case "Q2" -> "Apr - June";
            case "Q3" -> "Jul - Sep";
            case "Q4" -> "Oct - Dec";
            default -> "";
        };
    }
}
