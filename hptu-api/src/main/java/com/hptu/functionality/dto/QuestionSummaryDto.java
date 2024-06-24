package com.hptu.functionality.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record QuestionSummaryDto(
        Long id,
        @NotBlank String summary,
        @NotNull Integer minimumPreviousQuestionScore,
        @NotNull Integer questionOrderNumber,
        Long functionalityId,
        List<QuestionDto> questions,
        List<ScoreSummarySetUpDto> scoreSummaries
) implements Serializable, Comparable<QuestionSummaryDto> {
        @Override
        public int compareTo(QuestionSummaryDto o) {
                return Integer.compare(this.questionOrderNumber, o.questionOrderNumber());
        }
}
