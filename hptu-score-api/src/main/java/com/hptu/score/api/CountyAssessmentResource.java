package com.hptu.score.api;

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
    @Path("/save-county-assessment/{pillarCount}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveCountAssessment(CountyAssessmentStatus newAssessment, @PathParam("pillarCount") Integer pillarCount) {
        try {
            newAssessment.setStatus(CountyAssessmentStatus.Status.INCOMPLETE);
            newAssessment.setCreatedBy(getCurrentLoggedUser());
            newAssessment.setCountyName(getCountyByCode(newAssessment.getCountyCode()));
            CountyAssessmentStatus status = this.countyAssessmentService.saveCountyAssessment(newAssessment);
        } catch (Exception e) {
            return null;
        }

        return null;
    }
}
