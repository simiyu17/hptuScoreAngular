package com.hptu.score.api;

import com.hptu.score.dto.ApiResponseDto;
import com.hptu.score.dto.CountyAssessmentDto;
import com.hptu.score.entity.CountyAssessmentMetaData;
import com.hptu.score.service.CountyAssessmentService;
import com.hptu.score.util.CommonUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("api/v1/county-assessments")
@ApplicationScoped
public class CountyAssessmentResource extends CommonUtil {

    private final CountyAssessmentService countyAssessmentService;

    public CountyAssessmentResource(CountyAssessmentService countyAssessmentService) {
        this.countyAssessmentService = countyAssessmentService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CountyAssessmentMetaData> getAvailableAssessments(){
        return this.countyAssessmentService.getAvailableCountyAssessmentMetaDatas();
    }

    @GET
    @Path("{assessmentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssessmentById(@PathParam("assessmentId") Long assessmentId){
        return Response.ok(this.countyAssessmentService.findCountyAssessmentMetaDataById(assessmentId)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCountAssessment(@Valid CountyAssessmentDto newAssessment) {
        try {
            this.countyAssessmentService.createCountyAssessment(newAssessment);
            return Response.status(Response.Status.CREATED).entity(new ApiResponseDto(true, "County assessment was submitted!!")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.CREATED).entity(new ApiResponseDto(false, "An error occurred: "+e.getMessage())).build();
        }

    }
}
