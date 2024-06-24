package com.hptu.report.api;


import com.hptu.hptuassessment.dto.CountyDto;
import com.hptu.hptuassessment.service.CountyAssessmentService;
import com.hptu.report.dto.CountyAssessmentResultDetailedDto;
import com.hptu.report.dto.CountySummaryDto;
import com.hptu.shared.dto.ApiResponseDto;
import com.hptu.util.CommonUtil;
import com.hptu.util.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reports")
public class SummaryReportController {

    private final CountyAssessmentService assessmentService;

    @GetMapping("counties")
    public ResponseEntity<List<CountyDto>> getAvailablePillars(){
        return new ResponseEntity<>(CommonUtil.getKenyanCounties(), HttpStatus.OK);

    }

    @GetMapping("county-assessments-summary")
    public ResponseEntity<CountyAssessmentResultDetailedDto> getCountyAssessmentSummary(@RequestParam(value = "countyCode", required = false) String countyCode,
                                                                        @RequestParam(value = "assessmentQuarter", required = false) String assessmentQuarter,
                                                                        @RequestParam(value = "assessmentYear", required = false) String assessmentYear,
                                                                        @RequestParam(value = "pillarName", required = false) String pillarName){
        return new ResponseEntity<>(this.assessmentService.getAssessmentPerformanceSummary(assessmentYear, countyCode, assessmentQuarter, pillarName), HttpStatus.OK);
    }

    @GetMapping("county-assessments-summary-excel")
    public ResponseEntity<ApiResponseDto> getCountyAssessmentSummaryToExcel(@RequestParam(value = "countyCode", required = false) String countyCode,
                                                                            @RequestParam(value = "assessmentQuarter", required = false) String assessmentQuarter,
                                                                            @RequestParam(value = "assessmentYear", required = false) String assessmentYear) throws IOException {
        final var assessmentSummary = this.assessmentService.getAssessmentPerformanceSummary(assessmentYear, countyCode, assessmentQuarter, null);

        final var countySummaryDtos = assessmentSummary.summary();
        Map<String, List<CountySummaryDto>> categorySummary = new HashMap<>();
        for (CountySummaryDto summary: countySummaryDtos){
            final var pillarSummary = this.assessmentService.getAssessmentPerformanceSummary(assessmentYear, countyCode, assessmentQuarter, summary.getPillarName());
            categorySummary.put(summary.getPillarName(), pillarSummary.summary());
        }
        final var generator = new ExcelGenerator(countySummaryDtos, categorySummary, assessmentSummary.summaryTitle());
        return new ResponseEntity<>(new ApiResponseDto(true, generator.generateExcelBase64String()), HttpStatus.OK);
    }


    @GetMapping("export-to-excel")
    public ResponseEntity<ApiResponseDto> exportBase64ExcelFile(@RequestParam(value = "metaDataId") Long metaDataId,
                                          @RequestParam(value = "analysisByPillarTableTitle") String analysisByPillarTableTitle) throws IOException {
        List<CountySummaryDto> countySummaryDtos = this.assessmentService
                .getCountyAssessmentSummaryGroupedByPillar(metaDataId);
        Map<String, List<CountySummaryDto>> categorySummary = new HashMap<>();
        for (CountySummaryDto summary: countySummaryDtos){
            categorySummary.put(summary.getPillarName(), this.assessmentService
                    .getCountyAssessmentSummaryGroupedByCategory(metaDataId, summary.getPillarName()));
        }
        ExcelGenerator generator = new ExcelGenerator(countySummaryDtos, categorySummary, analysisByPillarTableTitle);
        return new ResponseEntity<>(new ApiResponseDto(true, generator.generateExcelBase64String()), HttpStatus.OK);
    }
}
