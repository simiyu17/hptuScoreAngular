package com.hptu.setup.exception;

import java.io.Serializable;

public class PillarException extends RuntimeException implements Serializable {

    public PillarException(String message) {
        super(message);
    }

    public PillarException(String message, Throwable cause) {
        super(message, cause);
    }
}
