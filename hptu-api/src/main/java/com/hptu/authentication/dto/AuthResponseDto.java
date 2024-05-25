package com.hptu.authentication.dto;

public record AuthResponseDto(
        boolean success,
        String message,
        String authToken
) {
}
