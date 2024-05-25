package com.hptu.score.api;

import com.hptu.score.dto.ApiResponseDto;
import com.hptu.score.dto.AssessmentChoiceDto;
import com.hptu.score.entity.AssessmentPillar;
import com.hptu.score.entity.AssessmentPillarCategory;
import com.hptu.score.service.AssessmentDefinitionService;
import com.hptu.score.util.CommonUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;

@Path("/v1/assessment-pillars")
@ApplicationScoped
public class AssessmentDefinitionResource extends CommonUtil {

    private final AssessmentDefinitionService assessmentDefinitionService;

    public AssessmentDefinitionResource(AssessmentDefinitionService assessmentDefinitionService) {
        this.assessmentDefinitionService = assessmentDefinitionService;
    }

    @GET
    @RolesAllowed(ROLE_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<AssessmentPillar> getAvailablePillars(){
        return this.assessmentDefinitionService.getAvailableAssessmentPillars();
    }

    @GET
    @Path("{pillarId}")
    @RolesAllowed(ROLE_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailablePillarById(@PathParam("pillarId") Long pillarId){
        return Response.ok(this.assessmentDefinitionService.findAssessmentPillarById(pillarId)).build();
    }

    @POST
    @RolesAllowed(ROLE_ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePillar(@Valid AssessmentPillar newPillar) {
        this.assessmentDefinitionService.createAssessmentPillar(newPillar);
        return Response.status(Response.Status.CREATED).entity(new ApiResponseDto(true, "Pillar Created!!")).build();
    }

    @PUT
    @Path("{pillarId}")
    @RolesAllowed(ROLE_ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePillar(@PathParam("pillarId") Long pillarId, @Valid  AssessmentPillar pillarUpdates) {
        this.assessmentDefinitionService.updateAssessmentPillar(pillarId, pillarUpdates);
        return Response.ok(new ApiResponseDto(true, "Pillar Created!!")).build();
    }

    @DELETE
    @Path("{pillarId}")
    @RolesAllowed(ROLE_ADMIN)
    public Response removePillar(@PathParam("pillarId") Long pillarId) {
            this.assessmentDefinitionService.deleteAssessmentPillar(pillarId);
            return Response.ok(new ApiResponseDto(true, "Pillar Deleted !!")).build();
    }

    @POST
    @Path("{pillarId}/categories")
    @RolesAllowed(ROLE_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCategory(@PathParam("pillarId") Long pillarId, @Valid AssessmentChoiceDto newPillarChoice) {
            try {
                if (!isCategoryChoiceFourMaxScore(newPillarChoice)){
                    return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponseDto(true, "Category choice 4 must contain the max score !!!!!")).build();
                }
                this.assessmentDefinitionService.addChoices(pillarId, newPillarChoice);
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponseDto(true, "Category name or position already exist !!!!!")).build();
            }
        return Response.ok(new ApiResponseDto(true, "Category successfully created!!")).build();
    }

    private boolean isCategoryChoiceFourMaxScore(AssessmentChoiceDto category){
        return category.choiceOneScore() <= category.choiceFourScore() &&
                category.choiceTwoScore() <= category.choiceFourScore() &&
                category.choiceThreeScore() <= category.choiceFourScore();
    }

    @PUT
    @Path("{pillarId}/categories/{categoryId}")
    @RolesAllowed(ROLE_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategory(@PathParam("pillarId") Long pillarId, @PathParam("categoryId") Long categoryId, AssessmentChoiceDto updatedCategory) {
        try {
            var currentPillar = this.assessmentDefinitionService.findAssessmentPillarById(pillarId);
            var pillarChoices = currentPillar.getAssessmentChoices();

            var choiceToUpdate = pillarChoices.stream().filter(c -> Objects.equals(c.getId(), categoryId)).findFirst();
            if(choiceToUpdate.isPresent()){
                AssessmentPillarCategory category = choiceToUpdate.get();
                category.updateCategory(updatedCategory);
                this.assessmentDefinitionService.createAssessmentPillar(currentPillar);
            }
            return Response.ok(new ApiResponseDto(true, "Successfully Updated!!")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponseDto(true, "An Error Occurred!!")).build();
        }

    }

    @GET
    @Path("{pillarId}/categories")
    @RolesAllowed(ROLE_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<AssessmentChoiceDto> getAvailableCategoriesByPillarId(@PathParam("pillarId") Long pillarId, @QueryParam("quarter") String quarter){
        return this.assessmentDefinitionService.getAvailableCategoriesByPillarId(pillarId, quarter);
    }


    @DELETE
    @Path("{pillarId}/categories/{categoryId}")
    @RolesAllowed(ROLE_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeCategory(@PathParam("pillarId") Long pillarId, @PathParam("categoryId") Long categoryId) {
        try {
            var currentPillar = this.assessmentDefinitionService.findAssessmentPillarById(pillarId);
            var pillarChoices = currentPillar.getAssessmentChoices();

            var choiceToRemove = pillarChoices.stream().filter(c -> Objects.equals(c.getId(), categoryId)).findFirst();
            if(choiceToRemove.isPresent()){
                pillarChoices.remove(choiceToRemove.get());
                currentPillar.setAssessmentChoices(pillarChoices);
                this.assessmentDefinitionService.createAssessmentPillar(currentPillar);
            }
            return Response.ok(new ApiResponseDto(true, "Successfully Deleted!!")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "An Error Occurred!!").build();
        }

    }
}
