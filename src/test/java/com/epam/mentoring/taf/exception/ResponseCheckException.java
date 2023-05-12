package com.epam.mentoring.taf.exception;

public class ResponseCheckException extends RuntimeException {

    public ResponseCheckException(String message) {
        super(message);
    }

    public ResponseCheckException(String message, Throwable cause) {
        super(message, cause);
    }

}
