package com.hptu.setup.service;

import com.hptu.setup.domain.AssessmentPillar;
import com.hptu.setup.domain.AssessmentPillarCategory;
import com.hptu.setup.domain.AssessmentPillarRepositoryWrapper;
import com.hptu.setup.dto.AssessmentChoiceDto;
import com.hptu.setup.exception.PillarException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AssessmentDefinitionServiceImpl implements AssessmentDefinitionService {

    private final AssessmentPillarRepositoryWrapper assessmentPillarRepository;


    @Override
    public AssessmentPillar createAssessmentPillar(AssessmentPillar pillar) {
        try {
            return this.assessmentPillarRepository.savePillar(pillar);
        } catch (Exception e){
            throw new PillarException("Pillar Name/Order already in use!!!!");
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
        throw new PillarException("Pillar Name/Order already in use!!!!");
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
                .orElseThrow(() -> new PillarException("No Pillar with Id"));
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
    public List<AssessmentChoiceDto> getAvailableCategoriesByPillarId(Long pillarId, String quarter) {
        AssessmentPillar pillar = this.findAssessmentPillarById(pillarId);
        List<AssessmentChoiceDto> choices = Objects.nonNull(pillar) ? pillar.getAssessmentChoices().stream()
                .map(c -> new AssessmentChoiceDto(c.getId(), c.getCategory(), c.getChoiceOne(),
                        c.getChoiceOneScore(), c.getChoiceTwo(), c.getChoiceTwoScore(), c.getChoiceThree(), c.getChoiceThreeScore(),
                        c.getChoiceFour(), c.getChoiceFourScore(), c.getCategoryOrder(),
                        StringUtils.isBlank(c.getAllowedQuarters()) ? List.of() : new ArrayList<>(List.of(c.getAllowedQuarters().replace("[", "").replace("]", "").replaceAll("\\s+", "").split(",")))))
                .sorted(Comparator.comparingInt(AssessmentChoiceDto::categoryOrder))
                .toList() : List.of();
        if (StringUtils.isNotBlank(quarter) && !choices.isEmpty()){
            choices = choices.stream()
                    .filter(c -> c.allowedQuarters().contains(quarter))
                    .toList();
        }
        return choices;
    }

    @Override
    public void deleteAssessmentPillar(Long pillarId) {
        AssessmentPillar pillar = this.findAssessmentPillarById(pillarId);
        this.assessmentPillarRepository.deleteAssessmentPillar(pillar.getId());
    }
}
