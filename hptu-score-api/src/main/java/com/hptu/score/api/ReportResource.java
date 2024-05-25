package com.hptu.score.api;

import com.hptu.score.dto.*;
import com.hptu.score.entity.BaseEntity;
import com.hptu.score.entity.CountyAssessmentMetaData;
import com.hptu.score.service.CountyAssessmentService;
import com.hptu.score.util.CommonUtil;
import com.hptu.score.util.ExcelGenerator;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Path("/v1/report")
@ApplicationScoped
public class ReportResource extends CommonUtil {

    private final CountyAssessmentService assessmentService;

    public ReportResource(CountyAssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @GET
    @Path("counties")
    @RolesAllowed({ROLE_ADMIN, ROLE_USER})
    @Produces(MediaType.APPLICATION_JSON)
    public List<CountyDto> getAvailablePillars(){
        return getKenyanCounties();
    }

    @GET
    @Path("county-assessments-summary")
    @RolesAllowed({ROLE_ADMIN, ROLE_USER})
    @Produces(MediaType.APPLICATION_JSON)
    public CountyAssessmentResultDetailedDto getCountyAssessmentSummary(@QueryParam(value = "countyCode") String countyCode,
                                                                               @QueryParam(value = "assessmentQuarter") String assessmentQuarter,
                                                                               @QueryParam(value = "assessmentYear") String assessmentYear){
        CountyAssessmentMetaData metaData = this.assessmentService.getAvailableCountyAssessmentMetaDatas().stream().max(Comparator.comparing(BaseEntity::getDateCreated)).orElse(null);
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
        return new CountyAssessmentResultDetailedDto(countySummaryDtos, summaryDataPoints, "");
    }

    @GET
    @Path("assessments-summary-per-pillar/{metaDataId}/{pillarName}")
    @RolesAllowed({ROLE_ADMIN, ROLE_USER})
    @Produces(MediaType.APPLICATION_JSON)
    public CountyAssessmentResultDetailedDto getCountyAssessmentSummaryPerPillar(@PathParam(value = "metaDataId") Long metaDataId,
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
        return new CountyAssessmentResultDetailedDto(countySummaryDtos, summaryDataPoints, "");
    }


    @GET
    @Path("export-to-excel")
    @RolesAllowed({ROLE_ADMIN, ROLE_USER})
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
