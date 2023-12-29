package com.hptu.score.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;

public class UserDto {

    private Long id;
    @NotBlank
    private String firstname;

    @NotBlank
    private String lastName;

    @Email
    private String username;

    @NotBlank
    private String designation;

    @NotBlank
    private String cellPhone;

    @NotBlank
    private String role;

    public UserDto(Long id, String firstname, String lastName, String username, String designation, String cellPhone, String role) {
        this.id = id;
        this.firstname = firstname;
        this.lastName = lastName;
        this.username = username;
        this.designation = designation;
        this.cellPhone = cellPhone;
        this.role = StringUtils.upperCase(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
