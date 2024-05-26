package com.hptu.setup.api;

import com.hptu.setup.domain.AssessmentPillar;
import com.hptu.setup.domain.AssessmentPillarCategory;
import com.hptu.setup.dto.AssessmentChoiceDto;
import com.hptu.setup.service.AssessmentDefinitionService;
import com.hptu.shared.dto.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/assessment-definitions")
public class AssessmentDefinitionController {

    private final AssessmentDefinitionService assessmentDefinitionService;


    @GetMapping
    public ResponseEntity<List<AssessmentPillar>>  getAvailablePillars(){
        return new ResponseEntity<>(this.assessmentDefinitionService.getAvailableAssessmentPillars(), HttpStatus.OK);
    }

    @GetMapping("{pillarId}")
    public ResponseEntity<AssessmentPillar>  getAvailablePillarById(@PathVariable("pillarId") Long pillarId){
        return new ResponseEntity<>(this.assessmentDefinitionService.findAssessmentPillarById(pillarId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> savePillar(@Valid @RequestBody AssessmentPillar newPillar) {
        this.assessmentDefinitionService.createAssessmentPillar(newPillar);
        return new ResponseEntity<>(new ApiResponseDto(true, "Pillar Created!!"), HttpStatus.CREATED);
    }

    @PutMapping("{pillarId}")
    public ResponseEntity<ApiResponseDto> updatePillar(@PathVariable("pillarId") Long pillarId, @Valid  @RequestBody AssessmentPillar pillarUpdates) {
        this.assessmentDefinitionService.updateAssessmentPillar(pillarId, pillarUpdates);
        return new ResponseEntity<>(new ApiResponseDto(true, "Pillar Updated!!"), HttpStatus.OK);
    }

    @DeleteMapping("{pillarId}")
    public ResponseEntity<ApiResponseDto> removePillar(@PathVariable("pillarId") Long pillarId) {
        this.assessmentDefinitionService.deleteAssessmentPillar(pillarId);
        return new ResponseEntity<>(new ApiResponseDto(true, "Pillar Deleted !!"), HttpStatus.NO_CONTENT);
    }

    @PostMapping("{pillarId}/categories")
    public ResponseEntity<ApiResponseDto> addCategory(@PathVariable("pillarId") Long pillarId, @Valid @RequestBody AssessmentChoiceDto newPillarChoice) {
        try {
            if (!isCategoryChoiceFourMaxScore(newPillarChoice)){
                return new ResponseEntity<>(new ApiResponseDto(false, "Category choice 4 must contain the max score !!!!!"), HttpStatus.BAD_REQUEST);
            }
            this.assessmentDefinitionService.addChoices(pillarId, newPillarChoice);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDto(false, "Category name or position already exist !!!!!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponseDto(true, "Category successfully created!!"), HttpStatus.CREATED);
    }

    private boolean isCategoryChoiceFourMaxScore(AssessmentChoiceDto category){
        return category.choiceOneScore() <= category.choiceFourScore() &&
                category.choiceTwoScore() <= category.choiceFourScore() &&
                category.choiceThreeScore() <= category.choiceFourScore();
    }

    @PutMapping("{pillarId}/categories/{categoryId}")
    public ResponseEntity<ApiResponseDto> updateCategory(@PathVariable("pillarId") Long pillarId, @PathVariable("categoryId") Long categoryId, @Valid @RequestBody AssessmentChoiceDto updatedCategory) {
        try {
            var currentPillar = this.assessmentDefinitionService.findAssessmentPillarById(pillarId);
            var pillarChoices = currentPillar.getAssessmentChoices();

            var choiceToUpdate = pillarChoices.stream().filter(c -> Objects.equals(c.getId(), categoryId)).findFirst();
            if(choiceToUpdate.isPresent()){
                AssessmentPillarCategory category = choiceToUpdate.get();
                category.updateCategory(updatedCategory);
                this.assessmentDefinitionService.createAssessmentPillar(currentPillar);
            }
            return new ResponseEntity<>(new ApiResponseDto(true, "Successfully Updated!!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDto(true, "An Error Occurred: "+e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("{pillarId}/categories")
    public ResponseEntity<List<AssessmentChoiceDto>>  getAvailableCategoriesByPillarId(@PathVariable("pillarId") Long pillarId, @RequestParam(value = "quarter", required = false) String quarter){
        return new ResponseEntity<>(this.assessmentDefinitionService.getAvailableCategoriesByPillarId(pillarId, quarter), HttpStatus.OK);
    }


    @DeleteMapping("{pillarId}/categories/{categoryId}")
    public ResponseEntity<ApiResponseDto> removeCategory(@PathVariable("pillarId") Long pillarId, @PathVariable("categoryId") Long categoryId) {
        try {
            var currentPillar = this.assessmentDefinitionService.findAssessmentPillarById(pillarId);
            var pillarChoices = currentPillar.getAssessmentChoices();

            var choiceToRemove = pillarChoices.stream().filter(c -> Objects.equals(c.getId(), categoryId)).findFirst();
            if(choiceToRemove.isPresent()){
                pillarChoices.remove(choiceToRemove.get());
                currentPillar.setAssessmentChoices(pillarChoices);
                this.assessmentDefinitionService.createAssessmentPillar(currentPillar);
            }
            return new ResponseEntity<>(new ApiResponseDto(true, "Successfully Deleted!!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDto(true, "An Error Occurred!!"), HttpStatus.BAD_REQUEST);
        }

    }
}
