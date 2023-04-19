package com.epam.mentoring.taf.exception;

public class DataUtilException extends RuntimeException {

    public DataUtilException(String message) {
        super(message);
    }

    public DataUtilException(String message, Throwable cause) {
        super(message, cause);
    }

}
