package com.hptu.hptuassessment.exception;

import java.io.Serializable;

public class CountyAssessmentException extends RuntimeException implements Serializable {

    public CountyAssessmentException(String message) {
        super(String.format("Error: %s", message));
    }

    public CountyAssessmentException(String message, Throwable cause) {
        super(message, cause);
    }

}
