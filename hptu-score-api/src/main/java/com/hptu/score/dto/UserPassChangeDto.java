package com.hptu.score.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class UserPassChangeDto implements Serializable {

    @NotBlank
    private String password;

    @NotBlank
    private String newPass;

    @NotBlank
    private String passConfirm;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getPassConfirm() {
        return passConfirm;
    }

    public void setPassConfirm(String passConfirm) {
        this.passConfirm = passConfirm;
    }
}
