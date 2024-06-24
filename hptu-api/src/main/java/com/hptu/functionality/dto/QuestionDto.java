package com.hptu.functionality.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record QuestionDto(
        Long id,
        @NotBlank String hptuQuestion,
        @NotNull Integer score,
        @NotNull Integer questionOrder,
        Long summaryId
) implements Serializable, Comparable<QuestionDto> {
    @Override
    public int compareTo(QuestionDto o) {
        return Integer.compare(this.questionOrder, o.questionOrder());
    }
}
