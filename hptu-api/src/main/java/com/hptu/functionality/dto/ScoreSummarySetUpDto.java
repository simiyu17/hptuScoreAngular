package com.hptu.functionality.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record ScoreSummarySetUpDto(
        Long id,
        @NotNull Integer from,
        @NotNull Integer to,
        @NotBlank String summaryColor,
        Long questionSummaryId
) implements Serializable{
}
