package com.hptu.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record UserDto(Long id, @NotBlank String firstName, @NotBlank String lastName, @Email String username,
                      @NotBlank String designation, @NotBlank String cellPhone, boolean isActive, boolean isAdmin) implements Serializable {
}
