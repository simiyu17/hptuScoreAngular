package com.hptu.score.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
