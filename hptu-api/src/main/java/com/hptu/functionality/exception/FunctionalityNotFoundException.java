package com.hptu.functionality.exception;

import java.io.Serializable;

public class FunctionalityNotFoundException extends RuntimeException implements Serializable {

    public FunctionalityNotFoundException(String message) {
        super(message);
    }

    public FunctionalityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
