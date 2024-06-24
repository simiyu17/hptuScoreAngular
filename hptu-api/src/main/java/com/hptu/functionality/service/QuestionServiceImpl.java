package com.hptu.functionality.service;

import com.hptu.functionality.domain.FunctionalityRepository;
import com.hptu.functionality.domain.Question;
import com.hptu.functionality.domain.QuestionSummary;
import com.hptu.functionality.domain.QuestionSummaryRepository;
import com.hptu.functionality.domain.ScoreSummarySetUp;
import com.hptu.functionality.dto.QuestionDto;
import com.hptu.functionality.dto.QuestionSummaryDto;
import com.hptu.functionality.dto.ScoreSummarySetUpDto;
import com.hptu.functionality.exception.FunctionalityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final FunctionalityRepository functionalityRepository;
    private final QuestionSummaryRepository questionSummaryRepository;


    @Override
    public void createQuestions(Long functionalityId, QuestionSummaryDto summaryDto) {
        try {
            var currentfunctionality = this.functionalityRepository.findById(functionalityId)
                    .orElseThrow(() -> new FunctionalityNotFoundException("No functionality found with Id"));
            final var questionSummary = QuestionSummary.createQuestionSummary(summaryDto, currentfunctionality);
            if (Objects.nonNull(summaryDto.questions()) && !summaryDto.questions().isEmpty()) {
                questionSummary.setQuestions(Question.createQuestions(summaryDto.questions(), questionSummary));
            }
            if (Objects.nonNull(summaryDto.scoreSummaries()) && !summaryDto.scoreSummaries().isEmpty()) {
                questionSummary.setScoreSummaries(ScoreSummarySetUp.createScoreSummarySetUps(summaryDto.scoreSummaries(), questionSummary));
            }
            currentfunctionality.addQuestionSummary(questionSummary);
            this.functionalityRepository.save(currentfunctionality);
        }catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void addQuestions(Long questionSummaryId, List<QuestionDto> questionDtos) {
        
    }

    @Override
    public void addScoreSummarySetUp(Long questionSummaryId, List<ScoreSummarySetUpDto> summarySetUpDtos) {
    }

    @Override
    public QuestionSummaryDto getQuestionById(Long questionSummaryId) {
        return null;
    }

    @Override
    public List<QuestionSummaryDto> getAllQuestions() {
        return List.of();
    }

    @Override
    public void deleteQuestionSummaryById(Long questionSummaryId) {

    }
}
