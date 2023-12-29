package com.hptu.score.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AssessmentChoiceDto (
        Long id,
        @NotBlank
        String category,
        @NotBlank
        String choiceOne,
        @Min(1)
        int choiceOneScore,
        @NotBlank
        String choiceTwo,
        @Min(1)
        int choiceTwoScore,
        @NotBlank
        String choiceThree,
        @Min(1)
        int choiceThreeScore,
        @NotBlank
        String choiceFour,
        @Min(1)
        int choiceFourScore,
        @Min(1)
        int categoryOrder
){}
