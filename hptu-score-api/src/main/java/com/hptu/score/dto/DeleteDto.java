package com.hptu.score.dto;

import jakarta.validation.constraints.NotNull;

public class DeleteDto {
    @NotNull
        private Long id;
        private Long id2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }
}
