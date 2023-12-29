package com.hptu.score.repository;

import com.hptu.score.entity.AssessmentPillar;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AssessmentPillarRepositoryWrapper {

    private final AssessmentPillarRepository assessmentPillarRepository;

    public AssessmentPillarRepositoryWrapper(AssessmentPillarRepository assessmentPillarRepository) {
        this.assessmentPillarRepository = assessmentPillarRepository;
    }

    @Transactional
    public AssessmentPillar savePillar(AssessmentPillar pillar) {
        return this.assessmentPillarRepository.save(pillar);
    }

    public Optional<AssessmentPillar> findAssessmentPillarById(Long pillarId){
        return this.assessmentPillarRepository.findById(pillarId);
    }

    public List<AssessmentPillar> getAvailableAssessmentPillars() {
        return this.assessmentPillarRepository.findAll(Sort.by(Sort.Order.asc("pillarOrder")));
    }

    @Transactional
    public void deleteAssessmentPillar(Long pillarId) {
        this.assessmentPillarRepository.deleteById(pillarId);
    }
}
