package com.hptu.score.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hptu.score.dto.ApiResponseDto;
import com.hptu.score.dto.CountyAssessmentDto2;
import com.hptu.score.entity.CountyAssessmentStatus;
import com.hptu.score.service.CountyAssessmentService;
import com.hptu.score.util.CommonUtil;
import jakarta.enterprise.context.ApplicationScoped;
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
    public List<CountyAssessmentStatus> getAvailableAssessments(){
        return this.countyAssessmentService.getAvailableCountyAssessmentStatuses();
    }

    @GET
    @Path("{assessmentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssessmentById(@PathParam("assessmentId") Long assessmentId){
        return Response.ok(this.countyAssessmentService.findCountyAssessmentStatusById(assessmentId)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCountAssessment(CountyAssessmentDto2 newAssessment) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(newAssessment));
            return Response.status(Response.Status.CREATED).entity(new ApiResponseDto(true, "County assessment was submitted!!")).build();
        } catch (Exception e) {
            return null;
        }

    }
}
