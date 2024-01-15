package com.hptu.score.exception;

import java.io.Serializable;

public class CountyAssessmentException extends RuntimeException implements Serializable {

    public CountyAssessmentException(String message) {
        super(message);
    }

    public CountyAssessmentException(String message, Throwable cause) {
        super(message, cause);
    }

}
