package com.hptu.functionality.service;

import com.hptu.functionality.dto.QuestionDto;
import com.hptu.functionality.dto.QuestionSummaryDto;
import com.hptu.functionality.dto.ScoreSummarySetUpDto;

import java.util.List;

public interface QuestionService {

    void createQuestions(Long functionalityId, QuestionSummaryDto summaryDto);

    void addQuestions(Long questionSummaryId, List<QuestionDto> questionDtos);

    void addScoreSummarySetUp(Long questionSummaryId, List<ScoreSummarySetUpDto> summarySetUpDtos);

    QuestionSummaryDto getQuestionById(Long questionSummaryId);

    List<QuestionSummaryDto> getAllQuestions();

    void deleteQuestionSummaryById(Long questionSummaryId);
}
