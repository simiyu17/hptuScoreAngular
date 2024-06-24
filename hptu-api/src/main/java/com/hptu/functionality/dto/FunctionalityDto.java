package com.hptu.functionality.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public record FunctionalityDto(
        Long id,
        @NotBlank String hptuName,
        String description,
        int functionalityOrder,
        List<QuestionSummaryDto> questions) implements Serializable, Comparable<FunctionalityDto> {
    @Override
    public int compareTo(FunctionalityDto o) {
        return Integer.compare(this.functionalityOrder, o.functionalityOrder());
    }
}
