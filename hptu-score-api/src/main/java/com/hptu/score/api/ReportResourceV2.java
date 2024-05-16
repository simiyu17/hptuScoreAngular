package com.hptu.score.api;

import com.hptu.score.dto.ApiResponseDto;
import com.hptu.score.dto.CountyAssessmentResultDetailedDto;
import com.hptu.score.dto.CountySummaryDto;
import com.hptu.score.service.CountyAssessmentService;
import com.hptu.score.util.CommonUtil;
import com.hptu.score.util.ExcelGenerator;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/v2/report")
@ApplicationScoped
public class ReportResourceV2 extends CommonUtil {

    private final CountyAssessmentService assessmentService;

    public ReportResourceV2(CountyAssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @GET
    @Path("county-assessments-summary")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public CountyAssessmentResultDetailedDto getCountyAssessmentSummary(@QueryParam(value = "countyCode") String countyCode,
                                                                        @QueryParam(value = "assessmentQuarter") String assessmentQuarter,
                                                                        @QueryParam(value = "assessmentYear") String assessmentYear){
        return this.assessmentService.getAssessmentPerformanceSummary(assessmentYear, countyCode, assessmentQuarter, null, true);
    }

    @GET
    @Path("assessments-summary-per-pillar/{pillarName}")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public CountyAssessmentResultDetailedDto getCountyAssessmentSummaryPerPillar(@PathParam(value = "pillarName") String pillarName,
                                                                                 @QueryParam(value = "countyCode") String countyCode,
                                                                                 @QueryParam(value = "assessmentQuarter") String assessmentQuarter,
                                                                                 @QueryParam(value = "assessmentYear") String assessmentYear){
        return this.assessmentService.getAssessmentPerformanceSummary(assessmentYear, countyCode, assessmentQuarter, pillarName, false);
    }

    @GET
    @Path("county-assessments-summary-excel")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCountyAssessmentSummaryToExcel(@QueryParam(value = "countyCode") String countyCode,
                                                                        @QueryParam(value = "assessmentQuarter") String assessmentQuarter,
                                                                        @QueryParam(value = "assessmentYear") String assessmentYear) throws IOException {
        if (StringUtils.isBlank(assessmentYear)){
            assessmentYear = String.valueOf(LocalDate.now(ZoneId.systemDefault()).getYear());
        }
        final var assessmentSummary = this.assessmentService.getAssessmentPerformanceSummary(assessmentYear, countyCode, assessmentQuarter, null, true);

        final var countySummaryDtos = assessmentSummary.summary();
        Map<String, List<CountySummaryDto>> categorySummary = new HashMap<>();
        for (CountySummaryDto summary: countySummaryDtos){
            final var pillarSummary = this.assessmentService.getAssessmentPerformanceSummary(assessmentYear, countyCode, assessmentQuarter, summary.getPillarName(), false);
            categorySummary.put(summary.getPillarName(), pillarSummary.summary());
        }
        final var generator = new ExcelGenerator(countySummaryDtos, categorySummary, assessmentSummary.summaryTitle());
        return Response.ok().entity(new ApiResponseDto(true, generator.generateExcelBase64String())).build();
    }


    @GET
    @Path("export-to-excel")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response exportBase64ExcelFile(@QueryParam(value = "metaDataId") Long metaDataId,
                                          @QueryParam(value = "analysisByPillarTableTitle") String analysisByPillarTableTitle) throws IOException {
        List<CountySummaryDto> countySummaryDtos = this.assessmentService
                .getCountyAssessmentSummaryGroupedByPillar(metaDataId);
        Map<String, List<CountySummaryDto>> categorySummary = new HashMap<>();
        for (CountySummaryDto summary: countySummaryDtos){
            categorySummary.put(summary.getPillarName(), this.assessmentService
                    .getCountyAssessmentSummaryGroupedByCategory(metaDataId, summary.getPillarName()));
        }
        ExcelGenerator generator = new ExcelGenerator(countySummaryDtos, categorySummary, analysisByPillarTableTitle);
        return Response.ok().entity(new ApiResponseDto(true, generator.generateExcelBase64String())).build();
    }
}
