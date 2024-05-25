package com.hptu.setup.service;


import com.hptu.setup.domain.AssessmentPillar;
import com.hptu.setup.dto.AssessmentChoiceDto;

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
