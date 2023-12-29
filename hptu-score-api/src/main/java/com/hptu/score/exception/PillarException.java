package com.hptu.score.exception;

import java.io.Serializable;

public class PillarException extends RuntimeException implements Serializable {

    private final int httpStatusCode;
    public PillarException(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public PillarException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public PillarException(String message, Throwable cause, int httpStatusCode) {
        super(message, cause);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
