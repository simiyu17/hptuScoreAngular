package com.hptu.score.service;

import com.hptu.score.dto.AssessmentChoiceDto;
import com.hptu.score.entity.AssessmentPillarCategory;
import com.hptu.score.entity.AssessmentPillar;

import java.util.List;

public interface AssessmentDefinitionService {

    AssessmentPillar createAssessmentPillar(AssessmentPillar pillar);

    AssessmentPillar updateAssessmentPillar(Long pillarId, AssessmentPillar updatedPillar);

    AssessmentPillar addChoices(Long pillarId, AssessmentChoiceDto choiceDto);

    AssessmentPillar findAssessmentPillarById(Long pillarId);

    AssessmentPillar updateAssessmentPillar(Long pillarId, String pillarName);

    List<AssessmentPillar> getAvailableAssessmentPillars();

    List<AssessmentChoiceDto> getAvailableCategoriesByPillarId(Long pillarId, String quarter);

    void deleteAssessmentPillar(Long pillarId);
}
