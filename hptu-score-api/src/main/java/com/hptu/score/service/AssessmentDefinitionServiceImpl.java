package com.hptu.score.service;

import com.hptu.score.dto.AssessmentChoiceDto;
import com.hptu.score.entity.AssessmentPillarCategory;
import com.hptu.score.entity.AssessmentPillar;
import com.hptu.score.exception.PillarException;
import com.hptu.score.repository.AssessmentPillarRepositoryWrapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class AssessmentDefinitionServiceImpl implements AssessmentDefinitionService {

    private final AssessmentPillarRepositoryWrapper assessmentPillarRepository;

    public AssessmentDefinitionServiceImpl(AssessmentPillarRepositoryWrapper assessmentPillarRepository) {
        this.assessmentPillarRepository = assessmentPillarRepository;
    }

    @Override
    public AssessmentPillar createAssessmentPillar(AssessmentPillar pillar) {
        try {
            return this.assessmentPillarRepository.savePillar(pillar);
        } catch (Exception e){
            throw new PillarException("Pillar Name/Order already in use!!!!", Response.Status.BAD_REQUEST.getStatusCode());
        }
    }

    @Override
    public AssessmentPillar updateAssessmentPillar(Long pillarId, AssessmentPillar updatedPillar) {
        AssessmentPillar currentPillar = this.findAssessmentPillarById(pillarId);
        try {
        if (StringUtils.isNotBlank(updatedPillar.getPillarName())){
            currentPillar.setPillarName(updatedPillar.getPillarName());
        }
        if (updatedPillar.getPillarOrder() > 0){
            currentPillar.setPillarOrder(updatedPillar.getPillarOrder());
        }
        return this.assessmentPillarRepository.savePillar(currentPillar);
    } catch (Exception e){
        throw new PillarException("Pillar Name/Order already in use!!!!", Response.Status.BAD_REQUEST.getStatusCode());
    }
    }

    @Transactional
    @Override
    public AssessmentPillar addChoices(Long pillarId, AssessmentChoiceDto choiceDto) {
        AssessmentPillar pillar = this.findAssessmentPillarById(pillarId);
        AssessmentPillarCategory choice = AssessmentPillarCategory.createAssessmentChoice(choiceDto);
        pillar.addAssessmentChoice(choice);
        return this.assessmentPillarRepository.savePillar(pillar);
    }

    @Override
    public AssessmentPillar findAssessmentPillarById(Long pillarId){
        return this.assessmentPillarRepository.findAssessmentPillarById(pillarId)
                .orElseThrow(() -> new PillarException("No Pillar with Id", Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Transactional
    @Override
    public AssessmentPillar updateAssessmentPillar(Long pillarId, String name) {
        AssessmentPillar pillar = this.findAssessmentPillarById(pillarId);
        pillar.setPillarName(name);
        return this.assessmentPillarRepository.savePillar(pillar);
    }

    @Override
    public List<AssessmentPillar> getAvailableAssessmentPillars() {
        return this.assessmentPillarRepository.getAvailableAssessmentPillars();
    }

    @Override
    public List<AssessmentChoiceDto> getAvailableCategoriesByPillarId(Long pillarId) {
        AssessmentPillar pillar = this.findAssessmentPillarById(pillarId);
        return Objects.nonNull(pillar) ? pillar.getAssessmentChoices().stream()
                .map(c -> new AssessmentChoiceDto(c.getId(), c.getCategory(), c.getChoiceOne(),
                        c.getChoiceOneScore(), c.getChoiceTwo(), c.getChoiceTwoScore(), c.getChoiceThree(), c.getChoiceThreeScore(),
                        c.getChoiceFour(), c.getChoiceFourScore(), c.getCategoryOrder()))
                .toList() : List.of();
    }

    @Override
    public void deleteAssessmentPillar(Long pillarId) {
        AssessmentPillar pillar = this.findAssessmentPillarById(pillarId);
        this.assessmentPillarRepository.deleteAssessmentPillar(pillar.getId());
    }
}
