package com.hptu.score.dto;

public record AuthResponseDto(
        boolean success,
        String message,
        String authToken
) {
}
