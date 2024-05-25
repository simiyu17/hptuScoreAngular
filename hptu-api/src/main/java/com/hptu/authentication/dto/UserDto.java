package com.hptu.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public record UserDto(Long id, @NotBlank String firstName, @NotBlank String lastName, @Email String username,
                      @NotBlank String designation, @NotBlank String cellPhone, boolean isActive, boolean isAdmin) implements Serializable {
}
