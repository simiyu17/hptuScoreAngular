package com.hptu.score.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
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
    public List<CountySummaryDto> getCountyAssessmentSummary(@QueryParam(value = "countyCode") String countyCode,
                                               @QueryParam(value = "assessmentQuarter") String assessmentQuarter,
                                               @QueryParam(value = "assessmentYear") String assessmentYear){
        CountyAssessmentMetaData metaData = this.assessmentService.getAvailableCountyAssessmentMetaDatas().stream().min(Comparator.comparing(BaseEntity::getDateCreated)).orElse(null);
        if (StringUtils.isNoneBlank(countyCode) && StringUtils.isNoneBlank(assessmentQuarter) && StringUtils.isNoneBlank(assessmentYear)){
            metaData = this.assessmentService.getCountyAssessmentByCodeYearAndQuarter(countyCode, assessmentQuarter, assessmentYear);
        }
        List<CountySummaryDto> countySummaryDtos = new ArrayList<>();
        if (Objects.nonNull(metaData)){
            countySummaryDtos = this.assessmentService
                    .getCountyAssessmentSummary(metaData.getId());
        }
        return countySummaryDtos;
    }

    @GET
    @Path("county-assessments-bar-data-points")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReportByPillar.PillarDataPointModel> getCountyAssessmentSummaryGraphDataPoints(
            @QueryParam(value = "countyCode") String countyCode,
            @QueryParam(value = "assessmentQuarter") String assessmentQuarter,
            @QueryParam(value = "assessmentYear") String assessmentYear){
        CountyAssessmentMetaData metaData = this.assessmentService.getAvailableCountyAssessmentMetaDatas().stream().min(Comparator.comparing(BaseEntity::getDateCreated)).orElse(null);
        if (StringUtils.isNoneBlank(countyCode) && StringUtils.isNoneBlank(assessmentQuarter) && StringUtils.isNoneBlank(assessmentYear)){
            metaData = this.assessmentService.getCountyAssessmentByCodeYearAndQuarter(countyCode, assessmentQuarter, assessmentYear);
        }
        List<ReportByPillar.PillarDataPointModel> summaryDataPoints = new ArrayList<>();
        if (Objects.nonNull(metaData)){
            List<CountySummaryDto> countySummaryDtos = this.assessmentService.getCountyAssessmentSummary(metaData.getId());
            summaryDataPoints = countySummaryDtos.stream()
                    .map(s -> new ReportByPillar.PillarDataPointModel(s.getPillarName(), s.getScorePercent())).toList();
        }
        return summaryDataPoints;
    }

    @GET
    @Path("export-to-excel")
    public void exportIntoExcelFile(HttpServletResponse response,
                                    @QueryParam(value = "statusId") Long statusId,
                                    @QueryParam(value = "countyName") String countyName,
                                    @QueryParam(value = "analysisByPillarTableTitle") String analysisByPillarTableTitle) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=%s-summary-%s.xlsx", countyName, currentDateTime);
        response.setHeader(headerKey, headerValue);

        List<CountySummaryDto> countySummaryDtos = this.assessmentService
                .getAssessmentSummaryGroupedByPillar(statusId).stream()
                .map(obj -> objectMapper.convertValue(obj, CountySummaryDto.Summary.class))
                .map(CountySummaryDto::new)
                .toList();
        Map<String, List<CountySummaryDto>> categorySummary = new HashMap<>();
        for (CountySummaryDto summary: countySummaryDtos){
            categorySummary.put(summary.getPillarName(), this.assessmentService
                    .getAssessmentCountSummaryGroupedByCategory(statusId, summary.getPillarName()).stream()
                    .map(obj -> objectMapper.convertValue(obj, CountySummaryDto.Summary.class))
                    .map(CountySummaryDto::new)
                    .toList());
        }

        ExcelGenerator generator = new ExcelGenerator(countySummaryDtos, categorySummary, analysisByPillarTableTitle);
        generator.generateExcelFile(response);
    }
}
