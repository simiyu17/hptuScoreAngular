package com.hptu.score.api;

import com.hptu.score.dto.ApiResponseDto;
import com.hptu.score.dto.AssessmentChoiceDto;
import com.hptu.score.entity.AssessmentPillar;
import com.hptu.score.entity.AssessmentPillarCategory;
import com.hptu.score.service.AssessmentDefinitionService;
import com.hptu.score.util.CommonUtil;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Path("api/v1/assessment-pillars")
@ApplicationScoped
@Authenticated
public class AssessmentDefinitionResource extends CommonUtil {

    private final AssessmentDefinitionService assessmentDefinitionService;

    public AssessmentDefinitionResource(AssessmentDefinitionService assessmentDefinitionService) {
        this.assessmentDefinitionService = assessmentDefinitionService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AssessmentPillar> getAvailablePillars(){
        return this.assessmentDefinitionService.getAvailableAssessmentPillars();
    }

    @GET
    @Path("{pillarId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailablePillarById(@PathParam("pillarId") Long pillarId){
        return Response.ok(this.assessmentDefinitionService.findAssessmentPillarById(pillarId)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePillar(@Valid AssessmentPillar newPillar) {
        this.assessmentDefinitionService.createAssessmentPillar(newPillar);
        return Response.status(Response.Status.CREATED).entity(new ApiResponseDto(true, "Pillar Created!!")).build();
    }

    @PUT
    @Path("{pillarId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePillar(@PathParam("pillarId") Long pillarId,  AssessmentPillar pillarUpdates) {
        this.assessmentDefinitionService.updateAssessmentPillar(pillarId, pillarUpdates);
        return Response.ok(new ApiResponseDto(true, "Pillar Created!!")).build();
    }

    @DELETE
    @Path("{pillarId}")
    public Response removePillar(@PathParam("pillarId") Long pillarId) {
            this.assessmentDefinitionService.deleteAssessmentPillar(pillarId);
            return Response.ok(new ApiResponseDto(true, "Pillar Deleted !!")).build();
    }

    @POST
    @Path("{pillarId}/categories")
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
    public Response updateCategory(@PathParam("pillarId") Long pillarId, @PathParam("categoryId") Long categoryId, AssessmentPillarCategory updatedCategory) {
        try {
            AssessmentPillar currentPillar = this.assessmentDefinitionService.findAssessmentPillarById(pillarId);
            Set<AssessmentPillarCategory> pillarChoices = currentPillar.getAssessmentChoices();

            Optional<AssessmentPillarCategory> choiceToUpdate = pillarChoices.stream().filter(c -> Objects.equals(c.getId(), categoryId)).findFirst();
            if(choiceToUpdate.isPresent()){
                AssessmentPillarCategory category = choiceToUpdate.get();
                category.setCategory(updatedCategory.getCategory());
                //:TODO Complete the update method
                this.assessmentDefinitionService.createAssessmentPillar(currentPillar);
            }
            return Response.ok(new ApiResponseDto(true, "Successfully Done!!")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponseDto(true, "An Error Occurred!!")).build();
        }

    }

    @GET
    @Path("{pillarId}/categories")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AssessmentChoiceDto> getAvailableCategoriesByPillarId(@PathParam("pillarId") Long pillarId){
        return this.assessmentDefinitionService.getAvailableCategoriesByPillarId(pillarId);
    }


    @DELETE
    @Path("{pillarId}/categories/{categoryId}")
    public Response removeCategory(@PathParam("pillarId") Long pillarId, @PathParam("categoryId") Long categoryId) {
        try {
            AssessmentPillar currentPillar = this.assessmentDefinitionService.findAssessmentPillarById(pillarId);
            Set<AssessmentPillarCategory> pillarChoices = currentPillar.getAssessmentChoices();

            Optional<AssessmentPillarCategory> choiceToremove = pillarChoices.stream().filter(c -> Objects.equals(c.getId(), categoryId)).findFirst();
            if(choiceToremove.isPresent()){
                pillarChoices.remove(choiceToremove.get());
                currentPillar.setAssessmentChoices(pillarChoices);
                this.assessmentDefinitionService.createAssessmentPillar(currentPillar);
            }
            return Response.ok("Successfully Done!!").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "An Error Occurred!!").build();
        }

    }
}
