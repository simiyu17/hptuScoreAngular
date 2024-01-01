package com.hptu.score.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hptu.score.dto.CountyAssessmentResultDetailedDto;
import com.hptu.score.dto.CountyDto;
import com.hptu.score.dto.CountySummaryDto;
import com.hptu.score.dto.ReportByPillar;
import com.hptu.score.entity.AssessmentPillar;
import com.hptu.score.entity.BaseEntity;
import com.hptu.score.entity.CountyAssessmentMetaData;
import com.hptu.score.service.CountyAssessmentService;
import com.hptu.score.util.CommonUtil;
import com.hptu.score.util.ExcelGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Path("api/v1/report")
@ApplicationScoped
public class ReportResource extends CommonUtil {

    private final CountyAssessmentService assessmentService;
    private final ObjectMapper objectMapper;

    public ReportResource(CountyAssessmentService assessmentService, ObjectMapper objectMapper) {
        this.assessmentService = assessmentService;
        this.objectMapper = objectMapper;
    }

    @GET
    @Path("counties")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CountyDto> getAvailablePillars(){
        return getKenyanCounties();
    }

    @GET
    @Path("county-assessments-summary")
    @Produces(MediaType.APPLICATION_JSON)
    public CountyAssessmentResultDetailedDto getCountyAssessmentSummary2(@QueryParam(value = "countyCode") String countyCode,
                                                                               @QueryParam(value = "assessmentQuarter") String assessmentQuarter,
                                                                               @QueryParam(value = "assessmentYear") String assessmentYear){
        CountyAssessmentMetaData metaData = this.assessmentService.getAvailableCountyAssessmentMetaDatas().stream().min(Comparator.comparing(BaseEntity::getDateCreated)).orElse(null);
        if (StringUtils.isNoneBlank(countyCode) && StringUtils.isNoneBlank(assessmentQuarter) && StringUtils.isNoneBlank(assessmentYear)){
            metaData = this.assessmentService.getCountyAssessmentByCodeYearAndQuarter(countyCode, assessmentQuarter, assessmentYear);
        }
        List<CountySummaryDto> countySummaryDtos = new ArrayList<>();
        List<ReportByPillar.PillarDataPointModel> summaryDataPoints = new ArrayList<>();
        if (Objects.nonNull(metaData)){
            countySummaryDtos = this.assessmentService
                    .getCountyAssessmentSummaryGroupedByPillar(metaData.getId());
            summaryDataPoints = countySummaryDtos.stream()
                    .map(s -> new ReportByPillar.PillarDataPointModel(s.getPillarName(), s.getScorePercent())).toList();
        }
        return new CountyAssessmentResultDetailedDto(countySummaryDtos, summaryDataPoints);
    }

    @GET
    @Path("assessments-summary-per-pillar/{metaDataId}/{pillarName}")
    @Produces(MediaType.APPLICATION_JSON)
    public CountyAssessmentResultDetailedDto getCountyAssessmentSummary(@PathParam(value = "metaDataId") Long metaDataId,
                                               @PathParam(value = "pillarName") String pillarName){
        CountyAssessmentMetaData metaData = this.assessmentService.getAvailableCountyAssessmentMetaDatas().stream().min(Comparator.comparing(BaseEntity::getDateCreated)).orElse(null);
        List<CountySummaryDto> countySummaryDtos = new ArrayList<>();
        List<ReportByPillar.PillarDataPointModel> summaryDataPoints = new ArrayList<>();
        if (Objects.nonNull(metaData)){
            countySummaryDtos = this.assessmentService
                    .getCountyAssessmentSummaryGroupedByCategory(metaData.getId(), pillarName);
            summaryDataPoints = countySummaryDtos.stream()
                    .map(s -> new ReportByPillar.PillarDataPointModel(s.getCategory(), BigDecimal.valueOf(s.getChoiceScore()))).toList();
        }
        return new CountyAssessmentResultDetailedDto(countySummaryDtos, summaryDataPoints);
    }

    @GET
    @Path("export-to-excel")
    public void exportIntoExcelFile(HttpServletResponse response,
                                    @QueryParam(value = "metaDataId") Long metaDataId,
                                    @QueryParam(value = "countyName") String countyName,
                                    @QueryParam(value = "analysisByPillarTableTitle") String analysisByPillarTableTitle) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=%s-summary-%s.xlsx", countyName, currentDateTime);
        response.setHeader(headerKey, headerValue);

        List<CountySummaryDto> countySummaryDtos = this.assessmentService
                .getCountyAssessmentSummaryGroupedByPillar(metaDataId);
        Map<String, List<CountySummaryDto>> categorySummary = new HashMap<>();
        for (CountySummaryDto summary: countySummaryDtos){
            categorySummary.put(summary.getPillarName(), this.assessmentService
                    .getCountyAssessmentSummaryGroupedByCategory(metaDataId, summary.getPillarName()));
        }

        ExcelGenerator generator = new ExcelGenerator(countySummaryDtos, categorySummary, analysisByPillarTableTitle);
        generator.generateExcelFile(response);
    }
}
